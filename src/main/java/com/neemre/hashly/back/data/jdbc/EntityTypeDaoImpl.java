package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.neemre.hashly.back.data.EntityTypeDao;
import com.neemre.hashly.back.domain.reference.EntityType;

public class EntityTypeDaoImpl implements EntityTypeDao {

	private static final String SQL_ENTITY_TYPE_READ = "SELECT * FROM entity_type WHERE " + 
			"entity_type_id = ?;";
	private static final String SQL_ENTITY_TYPE_READ_ALL = "SELECT * FROM entity_type;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
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
}