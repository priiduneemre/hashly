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

import com.neemre.hashly.backend.data.SourceFileDao;
import com.neemre.hashly.backend.domain.SourceFile;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class SourceFileDaoImpl implements SourceFileDao {

	private static final String SQL_SOURCE_FILE_CREATE = "INSERT INTO source_file (file_type_id,"
			+ " filename, size_bytes) VALUES (?, ?, ?);";
	private static final String SQL_SOURCE_FILE_READ = "SELECT * FROM source_file WHERE "
			+ "source_file_id = ?;";
	private static final String SQL_SOURCE_FILE_READ_ALL = "SELECT * FROM source_file;";
	private static final String SQL_SOURCE_FILE_UPDATE = "UPDATE source_file SET file_type_id = ?,"
			+ " filename = ?, size_bytes = ? WHERE source_file_id = ?;";
	private static final String SQL_SOURCE_FILE_DELETE = "DELETE FROM source_file WHERE "
			+ "source_file_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Integer create(final SourceFile sourceFile) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_SOURCE_FILE_CREATE, 
						new String[] {"source_file_id"});
				ps.setInt(1, sourceFile.getFileTypeId());
				ps.setString(2, sourceFile.getFilename());
				ps.setLong(3, sourceFile.getSizeBytes());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public SourceFile read(Integer sourceFileId) throws DataAccessException {
		SourceFile sourceFile = jdbcTemplate.queryForObject(SQL_SOURCE_FILE_READ, new Object[] {
				sourceFileId}, BeanPropertyRowMapper.newInstance(SourceFile.class));
		return sourceFile;
	}

	@Override
	public List<SourceFile> readAll() throws DataAccessException {
		List<SourceFile> sourceFiles = jdbcTemplate.query(SQL_SOURCE_FILE_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(SourceFile.class));
		return sourceFiles;
	}

	@Override
	public void update(SourceFile sourceFile) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_SOURCE_FILE_UPDATE, new Object[] {
				sourceFile.getFileTypeId(), sourceFile.getFilename(), sourceFile.getSizeBytes(),
				sourceFile.getSourceFileId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					SourceFile.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(Integer sourceFileId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_SOURCE_FILE_DELETE, sourceFileId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					SourceFile.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}