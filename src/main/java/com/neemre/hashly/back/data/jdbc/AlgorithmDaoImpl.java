package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.back.data.AlgorithmDao;
import com.neemre.hashly.back.domain.reference.Algorithm;

@Repository
public class AlgorithmDaoImpl implements AlgorithmDao {

	private static final String SQL_ALGORITHM_READ = "SELECT * FROM algorithm WHERE " 
			+ "algorithm_id = ?;";
	private static final String SQL_ALGORITHM_READ_ALL = "SELECT * FROM algorithm;";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public Algorithm read(int algorithmId) throws DataAccessException {
		Algorithm algorithm = jdbcTemplate.queryForObject(SQL_ALGORITHM_READ, 
				new Object[] {algorithmId}, BeanPropertyRowMapper.newInstance(Algorithm.class));
		return algorithm;
	}

	@Override
	public List<Algorithm> readAll() throws DataAccessException {
		List<Algorithm> algorithms = jdbcTemplate.query(SQL_ALGORITHM_READ_ALL , new Object[] {},
				BeanPropertyRowMapper.newInstance(Algorithm.class));
		return algorithms;
	}
}