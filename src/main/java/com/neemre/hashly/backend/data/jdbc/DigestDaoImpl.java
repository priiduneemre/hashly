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

import com.neemre.hashly.backend.data.DigestDao;
import com.neemre.hashly.backend.domain.Digest;
import com.neemre.hashly.common.misc.ResourceWrapper;
import com.neerme.hashly.common.ErrorCodes;

@Repository
public class DigestDaoImpl implements DigestDao {

	private static final String SQL_DIGEST_CREATE = "INSERT INTO digest (algorithm_id, " 
			+ "result_bundle_id, hex_value) VALUES (?, ?, ?);";
	private static final String SQL_DIGEST_READ = "SELECT * FROM digest WHERE digest_id = ?;";
	private static final String SQL_DIGEST_READ_ALL = "SELECT * FROM digest;";
	private static final String SQL_DIGEST_UPDATE = "UPDATE digest SET algorithm_id = ?, " 
			+ "result_bundle_id = ?, hex_value = ? WHERE digest_id = ?;";
	private static final String SQL_DIGEST_DELETE = "DELETE FROM digest WHERE digest_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ResourceWrapper resources;


	@Override
	public Integer create(final Digest digest) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_DIGEST_CREATE, 
						new String[] {"digest_id"});
				ps.setInt(1, digest.getAlgorithmId());
				ps.setInt(2, digest.getResultBundleId());
				ps.setString(3, digest.getHexValue());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public Digest read(Integer digestId) throws DataAccessException {
		Digest digest = jdbcTemplate.queryForObject(SQL_DIGEST_READ, new Object[] {digestId}, 
				BeanPropertyRowMapper.newInstance(Digest.class));
		return digest;
	}

	@Override
	public List<Digest> readAll() throws DataAccessException {
		List<Digest> digests = jdbcTemplate.query(SQL_DIGEST_READ_ALL, new Object[] {}, 
				BeanPropertyRowMapper.newInstance(Digest.class));
		return digests;
	}

	@Override
	public void update(Digest digest) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_DIGEST_UPDATE, new Object[] {
				digest.getAlgorithmId(), digest.getResultBundleId(), digest.getHexValue(), 
				digest.getDigestId()});
		if(rowsUpdated != 1) {
			String errorMsg = resources.getErrorMessage(ErrorCodes.RECORD_UPDATE_INCORRECT_RESULT_SIZE,
					new Object[]{1, Digest.class.getSimpleName(), getClass().getSimpleName(), 0});
			throw new IncorrectResultSizeDataAccessException(errorMsg, 1, 0);
		}
	}

	@Override
	public void delete(Integer digestId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_DIGEST_DELETE, digestId);
		if(rowsDeleted != 1) {
			String errorMsg = resources.getErrorMessage(ErrorCodes.RECORD_DELETE_INCORRECT_RESULT_SIZE,
					new Object[]{1, Digest.class.getSimpleName(), getClass().getSimpleName(), 0});
			throw new IncorrectResultSizeDataAccessException(errorMsg, 1, 0);
		}
	}
}