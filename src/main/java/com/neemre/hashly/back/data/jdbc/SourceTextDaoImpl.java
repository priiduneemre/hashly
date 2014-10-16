package com.neemre.hashly.back.data.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.neemre.hashly.back.data.SourceTextDao;
import com.neemre.hashly.back.domain.SourceText;

public class SourceTextDaoImpl implements SourceTextDao {

	private static final String SQL_SOURCE_TEXT_CREATE = "";
	private static final String SQL_SOURCE_TEXT_READ = "";
	private static final String SQL_SOURCE_TEXT_READ_ALL = "";
	private static final String SQL_SOURCE_TEXT_UPDATE = "";
	private static final String SQL_SOURCE_TEXT_DELETE = "";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public int create(final SourceText sourceText) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SourceText read(int sourceTextId) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SourceText> readAll() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(SourceText sourceText) throws DataAccessException {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(int sourceTextId) throws DataAccessException {
		// TODO Auto-generated method stub
	}
}