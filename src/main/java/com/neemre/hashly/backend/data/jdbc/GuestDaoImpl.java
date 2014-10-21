package com.neemre.hashly.backend.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.backend.data.GuestDao;
import com.neemre.hashly.backend.domain.Guest;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class GuestDaoImpl implements GuestDao {

	private static final String SQL_GUEST_CREATE = "INSERT INTO guest (ip_address) VALUES (?);";
	private static final String SQL_GUEST_READ = "SELECT * FROM guest WHERE guest_id = ?;";
	private static final String SQL_GUEST_READ_IP_ADDRESS = "SELECT * FROM guest WHERE "
			+ "ip_address = ?;";
	private static final String SQL_GUEST_READ_ALL = "SELECT * FROM guest;";
	private static final String SQL_GUEST_UPDATE = "UPDATE guest SET ip_address = ?, " 
			+ "visit_count = ? WHERE guest_id = ?;";
	private static final String SQL_GUEST_DELETE = "DELETE FROM guest WHERE guest_id = ?;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Integer create(final Guest guest) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_GUEST_CREATE, 
						new String[] {"guest_id"});
				ps.setString(1, guest.getIpAddress());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public Guest read(Integer guestId) throws DataAccessException {
		Guest guest = jdbcTemplate.queryForObject(SQL_GUEST_READ, new Object[] {guestId},
				BeanPropertyRowMapper.newInstance(Guest.class));
		return guest;
	}
	
	@Override
	public Guest readByIpAddress(String ipAddress) {
		Guest guest = jdbcTemplate.queryForObject(SQL_GUEST_READ_IP_ADDRESS, 
				new Object[]{ipAddress}, BeanPropertyRowMapper.newInstance(Guest.class));
		return guest;
	}

	@Override
	public List<Guest> readAll() throws DataAccessException {
		List<Guest> guests = jdbcTemplate.query(SQL_GUEST_READ_ALL, new Object[] {}, 
				BeanPropertyRowMapper.newInstance(Guest.class));
		return guests;
	}

	@Override
	public void update(Guest guest) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_GUEST_UPDATE, new Object[] {guest.getIpAddress(),
				guest.getVisitCount(), guest.getGuestId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					Guest.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(Integer guestId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_GUEST_DELETE, guestId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					Guest.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}