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

import com.neemre.hashly.back.data.EventDao;
import com.neemre.hashly.back.domain.Event;
import com.neerme.hashly.global.ExceptionMessage;

@Repository
public class EventDaoImpl implements EventDao {

	private static final String SQL_EVENT_READ = "SELECT * FROM event WHERE event_id = ?";
	private static final String SQL_EVENT_READ_ALL = "SELECT * FROM event;";
	private static final String SQL_EVENT_CREATE = "INSERT INTO event (event_type_id, " 
			+ "source_item_id, entity_type_id, guest_id) VALUES (?, ?, ?, ?);";
	private static final String SQL_EVENT_UPDATE = "UPDATE event SET event_type_id = ?, " 
			+ "source_item_id = ?, entity_type_id = ?, guest_id = ?, ocurred_at = ? " 
			+ "WHERE event_id = ?;";
	private static final String SQL_EVENT_DELETE = "DELETE FROM event WHERE event_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@Override
	public long create(final Event event) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_EVENT_CREATE,
						new String[] {"event_id"});
				ps.setInt(1, event.getEventTypeId());
				ps.setInt(2, event.getSourceItemId());
				ps.setInt(3, event.getEntityTypeId());
				ps.setInt(4, event.getGuestId());
				return ps;
			}
		}, idHolder); 
		return idHolder.getKey().longValue();
	}

	@Override
	public Event read(int eventId) throws DataAccessException {
		Event event = jdbcTemplate.queryForObject(SQL_EVENT_READ, new Object[] {eventId}, 
				BeanPropertyRowMapper.newInstance(Event.class));
		return event;
	}

	@Override
	public List<Event> readAll() throws DataAccessException {
		List<Event> events = jdbcTemplate.query(SQL_EVENT_READ_ALL, new Object[] {}, 
				BeanPropertyRowMapper.newInstance(Event.class));
		return events;
	}
	
	@Override
	public void update(Event event) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_EVENT_UPDATE, new Object[]{
				event.getEventTypeId(), event.getSourceItemId(), event.getEntityTypeId(),
				event.getGuestId(), event.getEventId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					Event.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);			
		}
	}
	
	@Override
	public void delete(int eventId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_EVENT_DELETE, eventId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					Event.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}