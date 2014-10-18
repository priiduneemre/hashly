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

import com.neemre.hashly.backend.data.AlgorithmDao;
import com.neemre.hashly.backend.domain.reference.Algorithm;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class AlgorithmDaoImpl implements AlgorithmDao {
	
	private static final String SQL_ALGORITHM_CREATE = "INSERT INTO algorithm (name, "
			+ "designer_name, digest_length_bits, description) VALUES (?, ?, ?, ?);";
	private static final String SQL_ALGORITHM_READ = "SELECT * FROM algorithm WHERE " 
			+ "algorithm_id = ?;";
	private static final String SQL_ALGORITHM_READ_ALL = "SELECT * FROM algorithm;";
	private static final String SQL_ALGORITHM_UPDATE = "UPDATE algorithm SET name = ?, " 
			+ "designer_name = ?, digest_length_bits = ?, description = ? WHERE algorithm_id = ?;";
	private static final String SQL_ALGORITHM_DELETE = "DELETE FROM algorithm WHERE "
			+ "algorithm_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public int create(final Algorithm algorithm) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_ALGORITHM_CREATE, 
						new String[] {"algorithm_id"});
				ps.setString(1, algorithm.getName());
				ps.setString(2, algorithm.getDesignerName());
				ps.setInt(3, algorithm.getDigestLengthBits());
				ps.setString(4, algorithm.getDescription());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}
	
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
	
	@Override
	public void update(Algorithm algorithm) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_ALGORITHM_UPDATE, new Object[] {
				algorithm.getName(), algorithm.getDesignerName(), algorithm.getDigestLengthBits(),
				algorithm.getDescription(), algorithm.getAlgorithmId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					Algorithm.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(int algorithmId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_ALGORITHM_DELETE, algorithmId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					Algorithm.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}