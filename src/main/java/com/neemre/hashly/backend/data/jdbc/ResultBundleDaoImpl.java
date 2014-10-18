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

import com.neemre.hashly.backend.data.ResultBundleDao;
import com.neemre.hashly.backend.domain.ResultBundle;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class ResultBundleDaoImpl implements ResultBundleDao {

	private static final String SQL_RESULT_BUNDLE_CREATE = "INSERT INTO result_bundle (guest_id, "
			+ "permacode) VALUES (?, ?);";
	private static final String SQL_RESULT_BUNDLE_READ = "SELECT * FROM result_bundle WHERE "
			+ "result_bundle_id = ?;";
	private static final String SQL_RESULT_BUNDLE_READ_ALL = "SELECT * FROM result_bundle;";
	private static final String SQL_RESULT_BUNDLE_UPDATE = "UPDATE result_bundle SET "
			+ "guest_id = ?, permacode = ?, view_count = ? WHERE result_bundle_id = ?;";
	private static final String SQL_RESULT_BUNDLE_DELETE = "DELETE FROM result_bundle WHERE "
			+ "result_bundle_id = ?;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public int create(final ResultBundle resultBundle) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_RESULT_BUNDLE_CREATE, 
						new String[] {"result_bundle_id"});
				ps.setInt(1, resultBundle.getGuestId());
				ps.setString(2, resultBundle.getPermacode());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public ResultBundle read(int resultBundleId) throws DataAccessException {
		ResultBundle resultBundle = jdbcTemplate.queryForObject(SQL_RESULT_BUNDLE_READ, 
				new Object[] {resultBundleId}, BeanPropertyRowMapper.newInstance(
						ResultBundle.class));
		return resultBundle;
	}

	@Override
	public List<ResultBundle> readAll() throws DataAccessException {
		List<ResultBundle> resultBundles = jdbcTemplate.query(SQL_RESULT_BUNDLE_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(ResultBundle.class));
		return resultBundles;
	}

	@Override
	public void update(ResultBundle resultBundle) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_RESULT_BUNDLE_UPDATE, new Object[] {
				resultBundle.getGuestId(), resultBundle.getPermacode(), 
				resultBundle.getViewCount(), resultBundle.getResultBundleId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					ResultBundle.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(int resultBundleId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_RESULT_BUNDLE_DELETE, resultBundleId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					ResultBundle.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}