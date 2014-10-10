package com.neemre.hashly.back.service;

import java.util.List;

import com.neemre.hashly.back.domain.Entity;

public interface Service<T extends Entity> {
	
	T findById(int entityId);
	
	List<T> findAll();
	
	T save(T entity);
	
	void delete(int entityId);
}