package com.neemre.hashly.back.data.jdbc;

import java.io.Serializable;

import com.neemre.hashly.back.data.GenericDao;

public class GenericDaoImpl<T, PK  extends Serializable> implements GenericDao<T, PK> {

	private Class<T> type;
	
	
	public GenericDaoImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	public PK create(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T read(Serializable entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Object transientEntity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(Object persistentEntity) {
		// TODO Auto-generated method stub	
	}
}