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

import com.neemre.hashly.backend.data.TextDigestDao;
import com.neemre.hashly.backend.domain.TextDigest;
import com.neemre.hashly.common.misc.ResourceWrapper;
import com.neerme.hashly.common.ErrorCodes;

@Repository
public class TextDigestDaoImpl implements TextDigestDao {

	private static final String SQL_TEXT_DIGEST_CREATE = "INSERT INTO text_digest (digest_id, " 
			+ "source_text_id) VALUES (?, ?);";
	private static final String SQL_TEXT_DIGEST_READ = "SELECT * FROM text_digest WHERE " 
			+ "digest_id = ?;";
	private static final String SQL_TEXT_DIGEST_READ_ALL = "SELECT * FROM text_digest;";
	private static final String SQL_TEXT_DIGEST_UPDATE = "UPDATE text_digest SET digest_id = ?, " 
			+ "source_text_id = ? WHERE digest_id = ?;";
	private static final String SQL_TEXT_DIGEST_DELETE = "DELETE FROM text_digest WHERE " 
			+ "digest_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ResourceWrapper resources;


	@Override
	public Integer create(final TextDigest textDigest) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_TEXT_DIGEST_CREATE, 
						new String[] {"digest_id"});
				ps.setInt(1, textDigest.getDigestId());
				ps.setInt(2, textDigest.getSourceTextId());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public TextDigest read(Integer digestId) throws DataAccessException {
		TextDigest textDigest = jdbcTemplate.queryForObject(SQL_TEXT_DIGEST_READ, new Object[] {
				digestId}, BeanPropertyRowMapper.newInstance(TextDigest.class));
		return textDigest;
	}

	@Override
	public List<TextDigest> readAll() throws DataAccessException {
		List<TextDigest> textDigests = jdbcTemplate.query(SQL_TEXT_DIGEST_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(TextDigest.class));
		return textDigests;
	}

	@Override
	public void update(TextDigest textDigest) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_TEXT_DIGEST_UPDATE, new Object[] {
				textDigest.getDigestId(), textDigest.getSourceTextId(), textDigest.getDigestId()});
		if(rowsUpdated != 1) {
			String errorMsg = resources.getErrorMessage(ErrorCodes.RECORD_UPDATE_INCORRECT_RESULT_SIZE,
					new Object[]{1, TextDigest.class.getSimpleName(), getClass().getSimpleName(), 0});
			throw new IncorrectResultSizeDataAccessException(errorMsg, 1, 0);
		}	
	}

	@Override
	public void delete(Integer digestId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_TEXT_DIGEST_DELETE, digestId);
		if(rowsDeleted != 1) {

			String errorMsg = resources.getErrorMessage(ErrorCodes.RECORD_DELETE_INCORRECT_RESULT_SIZE,
					new Object[]{1, TextDigest.class.getSimpleName(), getClass().getSimpleName(), 0});
			throw new IncorrectResultSizeDataAccessException(errorMsg, 1, 0);
		}
	}
}