package com.neemre.hashly.backend.data;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, PK extends Serializable> {
	
	PK create(T entity);
	
	T read(PK entityId);

	List<T> readAll();

	void update(T entity);
	
	void delete(PK entityId);
}