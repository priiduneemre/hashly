package com.neemre.hashly.back.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.back.data.GuestDao;
import com.neemre.hashly.back.domain.Guest;

@Repository
public class GuestDaoImpl implements GuestDao {

	private static final String SQL_GUEST_CREATE = "INSERT INTO guest (ip_address, visit_count) " 
			+ "VALUES (?, ?);";
	private static final String SQL_GUEST_READ = "SELECT * FROM guest WHERE guest_id = ?;";
	private static final String SQL_GUEST_READ_ALL = "SELECT * FROM guest;";
	private static final String SQL_GUEST_UPDATE = "UPDATE guest SET guest_id = ?, ip_address = ?, " + 
			"visit_count = ?;";
	private static final String SQL_GUEST_DELETE = "DELETE FROM guest WHERE guest_id = ?;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public int create(final Guest guest) {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) 
							throws SQLException {
						PreparedStatement ps = connection.prepareStatement(SQL_GUEST_CREATE, 
								new String[] {"ip_address", "visist_count"});
						ps.setString(1, guest.getIpAddress());
						ps.setInt(2, guest.getVisitCount());
						return ps;
					}
				}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public Guest read(int guestId) {
		//TODO: Handle DataAccessException when the resultset comes up empty etc
		Guest guest = jdbcTemplate.queryForObject(SQL_GUEST_READ, new Object[] {guestId}, 
				BeanPropertyRowMapper.newInstance(Guest.class));
		return guest;
	}

	@Override
	public List<Guest> readAll() {
		//TODO: Handle DataAccessException when the resultset comes up empty etc
		List<Guest> guests = jdbcTemplate.query(SQL_GUEST_READ_ALL, new Object[] {}, 
				BeanPropertyRowMapper.newInstance(Guest.class));
		return guests;
	}

	@Override
	public void update(Guest guest) {
		jdbcTemplate.update(SQL_GUEST_UPDATE);
	}

	@Override
	public void delete(int guestId) {
		jdbcTemplate.update(SQL_GUEST_DELETE, guestId);
	}
}