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

import com.neemre.hashly.backend.data.SourceTextDao;
import com.neemre.hashly.backend.domain.SourceText;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class SourceTextDaoImpl implements SourceTextDao {

	private static final String SQL_SOURCE_TEXT_CREATE = "INSERT INTO source_text (contents) " 
			+ "VALUES (?);";
	private static final String SQL_SOURCE_TEXT_READ = "SELECT * FROM source_text WHERE "
			+ "source_text_id = ?;";
	private static final String SQL_SOURCE_TEXT_READ_ALL = "SELECT * FROM source_text;";
	private static final String SQL_SOURCE_TEXT_UPDATE = "UPDATE source_text SET contents = ? "
			+ "WHERE source_text_id = ?;";
	private static final String SQL_SOURCE_TEXT_DELETE = "DELETE FROM source_text WHERE "
			+ "source_text_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public int create(final SourceText sourceText) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_SOURCE_TEXT_CREATE, 
						new String[] {"source_text_id"});
				ps.setString(1, sourceText.getContents());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public SourceText read(int sourceTextId) throws DataAccessException {
		SourceText sourceText = jdbcTemplate.queryForObject(SQL_SOURCE_TEXT_READ, new Object[] {
				sourceTextId}, BeanPropertyRowMapper.newInstance(SourceText.class));
		return sourceText;
	}

	@Override
	public List<SourceText> readAll() throws DataAccessException {
		List<SourceText> sourceTexts = jdbcTemplate.query(SQL_SOURCE_TEXT_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(SourceText.class));
		return sourceTexts;
	}

	@Override
	public void update(SourceText sourceText) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_SOURCE_TEXT_UPDATE, new Object[] {
				sourceText.getContents(), sourceText.getSourceTextId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					SourceText.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(int sourceTextId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_SOURCE_TEXT_DELETE, sourceTextId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					SourceText.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}