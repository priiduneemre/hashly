package com.neemre.hashly.backend.data;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, PK extends Serializable> {
	
	PK create(T record);
	
	T read(PK recordId);

	List<T> readAll();

	void update(T record);
	
	void delete(PK recordId);
}