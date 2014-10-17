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

import com.neemre.hashly.back.data.EntityTypeDao;
import com.neemre.hashly.back.domain.reference.EntityType;
import com.neerme.hashly.global.ExceptionMessage;

@Repository
public class EntityTypeDaoImpl implements EntityTypeDao {

	private static final String SQL_ENTITY_TYPE_CREATE = "INSERT INTO entity_type (code, label) "
			+ "VALUES (?, ?);";
	private static final String SQL_ENTITY_TYPE_READ = "SELECT * FROM entity_type WHERE " + 
			"entity_type_id = ?;";
	private static final String SQL_ENTITY_TYPE_READ_ALL = "SELECT * FROM entity_type;";
	private static final String SQL_ENTITY_TYPE_UPDATE = "UPDATE entity_type SET code = ?, "
			+ "label = ? WHERE entity_type_id = ?;";
	private static final String SQL_ENTITY_TYPE_DELETE = "DELETE FROM entity_type WHERE "
			+ "entity_type_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public int create(final EntityType entityType) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_ENTITY_TYPE_CREATE, 
						new String[] {"entity_type_id"});
				ps.setString(1, entityType.getCode());
				ps.setString(2, entityType.getLabel());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public EntityType read(int entityTypeId) throws DataAccessException {
		EntityType entityType = jdbcTemplate.queryForObject(SQL_ENTITY_TYPE_READ, 
				new Object[] {entityTypeId}, BeanPropertyRowMapper.newInstance(EntityType.class));
		return entityType;
	}

	@Override
	public List<EntityType> readAll() throws DataAccessException {
		List<EntityType> entityTypes = jdbcTemplate.query(SQL_ENTITY_TYPE_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(EntityType.class));
		return entityTypes;
	}

	@Override
	public void update(EntityType entityType) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_ENTITY_TYPE_UPDATE, new Object[] {
				entityType.getCode(), entityType.getLabel(), entityType.getEntityTypeId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					EntityType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(int entityTypeId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_ENTITY_TYPE_DELETE, entityTypeId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					EntityType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}