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

import com.neemre.hashly.backend.data.FileDigestDao;
import com.neemre.hashly.backend.domain.FileDigest;
import com.neerme.hashly.common.ExceptionMessage;

@Repository
public class FileDigestDaoImpl implements FileDigestDao {

	private static final String SQL_FILE_DIGEST_CREATE = "INSERT INTO file_digest (digest_id, "
			+ "source_file_id) VALUES (?, ?);";
	private static final String SQL_FILE_DIGEST_READ = "SELECT * FROM file_digest WHERE "
			+ "digest_id = ?;";
	private static final String SQL_FILE_DIGEST_READ_ALL = "SELECT * FROM file_digest;";
	private static final String SQL_FILE_DIGEST_UPDATE = "UPDATE file_digest SET digest_id = ?, "
			+ "source_file_id = ? WHERE digest_id = ?;";
	private static final String SQL_FILE_DIGEST_DELETE = "DELETE FROM file_digest WHERE "
			+ "digest_id = ?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public Integer create(final FileDigest fileDigest) throws DataAccessException {
		KeyHolder idHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) 
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL_FILE_DIGEST_CREATE, 
						new String[] {"digest_id"});
				ps.setInt(1, fileDigest.getDigestId());
				ps.setInt(2, fileDigest.getSourceFileId());
				return ps;
			}
		}, idHolder);
		return idHolder.getKey().intValue();
	}

	@Override
	public FileDigest read(Integer digestId) throws DataAccessException {
		FileDigest fileDigest = jdbcTemplate.queryForObject(SQL_FILE_DIGEST_READ, new Object[] {
				digestId}, BeanPropertyRowMapper.newInstance(FileDigest.class));
		return fileDigest;
	}

	@Override
	public List<FileDigest> readAll() throws DataAccessException {
		List<FileDigest> fileDigests = jdbcTemplate.query(SQL_FILE_DIGEST_READ_ALL, 
				new Object[] {}, BeanPropertyRowMapper.newInstance(FileDigest.class));
		return fileDigests;
	}

	@Override
	public void update(FileDigest fileDigest) throws DataAccessException {
		int rowsUpdated = jdbcTemplate.update(SQL_FILE_DIGEST_UPDATE, new Object[] {
				fileDigest.getDigestId(), fileDigest.getSourceFileId(), fileDigest.getDigestId()});
		if(rowsUpdated != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_UPDATE_INCORRECT_RESULT_SIZE, 1, 
					FileDigest.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}

	@Override
	public void delete(Integer digestId) throws DataAccessException {
		int rowsDeleted = jdbcTemplate.update(SQL_FILE_DIGEST_DELETE, digestId);
		if(rowsDeleted != 1) {
			throw new IncorrectResultSizeDataAccessException(String.format(
					ExceptionMessage.RECORD_DELETE_INCORRECT_RESULT_SIZE, 1,
					FileDigest.class.getSimpleName(), getClass().getSimpleName(), 0), 1, 0);
		}
	}
}