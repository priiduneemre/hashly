package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.neemre.hashly.back.data.FileTypeDao;
import com.neemre.hashly.back.domain.reference.FileType;

@Repository
public class FileTypeDaoImpl implements FileTypeDao {

	private static final String SQL_FILE_TYPE_READ = "SELECT * FROM file_type WHERE " + 
			"file_type_id = ?;";
	private static final String SQL_FILE_TYPE_READ_ALL = "SELECT * FROM file_type;";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public FileType read(int fileTypeId) throws DataAccessException {
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
}