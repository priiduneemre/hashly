package com.neemre.hashly.back.data;

import java.io.Serializable;

public interface GenericDao <T, PK extends Serializable> {
	
	PK create(T entity);
	
	T read(PK entityId);
	
	void update(T transientEntity);
	
	void delete(T persistentEntity);
}