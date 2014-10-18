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

import com.neemre.hashly.backend.data.FileTypeDao;
import com.neemre.hashly.backend.domain.reference.FileType;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class FileTypeDaoImpl implements FileTypeDao {

	private static final String SQL_FILE_TYPE_CREATE = "INSERT INTO file_type (extension, label) "
			+ "VALUES (?, ?);";
	private static final String SQL_FILE_TYPE_READ = "SELECT * FROM file_type WHERE "
			+ "file_type_id = ?;";
	private static final String SQL_FILE_TYPE_READ_ALL = "SELECT * FROM file_type;";
	private static final String SQL_FILE_TYPE_UPDATE = "UPDATE file_type SET extension = ?, "
			+ "label = ? WHERE file_type_id = ?;";
	private static final String SQL_FILE_TYPE_DELETE = "DELETE FROM file_type WHERE "
			+ "file_type_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Integer create(final FileType fileType) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_FILE_TYPE_CREATE, 
						new String[] {"file_type_id"});
				ps.setString(1, fileType.getExtension());
				ps.setString(2, fileType.getLabel());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public FileType read(Integer fileTypeId) throws DataAccessException {
		FileType fileType = jdbcTemplate.queryForObject(SQL_FILE_TYPE_READ, 
				new Object[] {fileTypeId}, BeanPropertyRowMapper.newInstance(FileType.class));
		return fileType;
	}

	@Override
	public List<FileType> readAll() throws DataAccessException {
		List<FileType> fileTypes = jdbcTemplate.query(SQL_FILE_TYPE_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(FileType.class));
		return fileTypes;
	}

	@Override
	public void update(FileType fileType) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_FILE_TYPE_UPDATE, new Object[] {
				fileType.getExtension(), fileType.getLabel(), fileType.getFileTypeId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					FileType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(Integer fileTypeId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_FILE_TYPE_DELETE, fileTypeId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					FileType.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}