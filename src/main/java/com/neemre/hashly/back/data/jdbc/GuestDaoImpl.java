package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.back.data.GuestDao;
import com.neemre.hashly.back.domain.Guest;

@Repository
public class GuestDaoImpl implements GuestDao {

	private static final String SQL_GUEST_READ ="SELECT * FROM guest WHERE guest_id = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public int create(Guest guest) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Guest read(int guestId) {
		//TODO: Handle DataAccessException when resultset empty etc
		Guest guest = jdbcTemplate.queryForObject(SQL_GUEST_READ, new Object[] {guestId}, 
				BeanPropertyRowMapper.newInstance(Guest.class));
		return guest;
	}

	@Override
	public List<Guest> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Guest guest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int guestId) {
		// TODO Auto-generated method stub
		
	}
}