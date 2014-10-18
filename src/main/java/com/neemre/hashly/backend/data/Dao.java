package com.neemre.hashly.backend.data;

import java.util.List;

public interface Dao<T> {
	
	int create(T entity);
	
	T read(int entityId);

	List<T> readAll();

	void update(T entity);
	
	void delete(int entityId);
}