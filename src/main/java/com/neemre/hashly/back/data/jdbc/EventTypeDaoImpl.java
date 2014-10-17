package com.neemre.hashly.back.data.jdbc;

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

import com.neemre.hashly.back.data.EventTypeDao;
import com.neemre.hashly.back.domain.reference.EventType;
import com.neerme.hashly.global.ExceptionMessage;

@Repository
public class EventTypeDaoImpl implements EventTypeDao {

	private static final String SQL_EVENT_TYPE_CREATE = "INSERT INTO event_type (code, label) "
			+ "VALUES (?, ?);";
	private static final String SQL_EVENT_TYPE_READ = "SELECT * FROM event_type WHERE "
			+ "event_type_id = ?;";
	private static final String SQL_EVENT_TYPE_READ_ALL = "SELECT * FROM event_type;";
	private static final String SQL_EVENT_TYPE_UPDATE = "UPDATE event_type SET code = ?, label = ?"
			+ "WHERE event_type_id = ?;";
	private static final String SQL_EVENT_TYPE_DELETE = "DELETE FROM event_type WHERE "
			+ "event_type_id = ?;";
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public int create(final EventType eventType) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_EVENT_TYPE_CREATE, 
						new String[] {"event_type_id"});
				ps.setString(1, eventType.getCode());
				ps.setString(2, eventType.getLabel());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}
	
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
	
	@Override
	public void update(EventType eventType) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_EVENT_TYPE_UPDATE, new Object[] {
				eventType.getCode(), eventType.getLabel(), eventType.getEventTypeId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					EventType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(int eventTypeId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_EVENT_TYPE_DELETE, eventTypeId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					EventType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}