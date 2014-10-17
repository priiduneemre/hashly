package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.back.data.EventTypeDao;
import com.neemre.hashly.back.domain.reference.EventType;

@Repository
public class EventTypeDaoImpl implements EventTypeDao {

	private static final String SQL_EVENT_TYPE_READ = "SELECT * FROM event_type WHERE " + 
			"event_type_id = ?;";
	private static final String SQL_EVENT_TYPE_READ_ALL = "SELECT * FROM event_type;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public EventType read(int eventTypeId) throws DataAccessException {
		EventType eventType = jdbcTemplate.queryForObject(SQL_EVENT_TYPE_READ, 
				new Object[] {eventTypeId}, BeanPropertyRowMapper.newInstance(EventType.class));
		return eventType;
	}

	@Override
	public List<EventType> readAll() throws DataAccessException {
		List<EventType> eventTypes = jdbcTemplate.query(SQL_EVENT_TYPE_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(EventType.class));
		return eventTypes;
	}
}