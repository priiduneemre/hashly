package com.neemre.hashly.back.service;

import java.util.List;

import com.neemre.hashly.back.domain.Entity;

public interface Service<T extends Entity> {
	
	public T findById(int entityId);
	
	public List<T> findAll();
	
	public T create(T entity);
	
	public void delete(int entityId);
}