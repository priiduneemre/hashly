package com.neemre.hashly.backend.service;

import java.io.Serializable;
import java.util.List;

public interface Service<T, ID extends Serializable> {

	T findById(ID entityId);
	
	List<T> findAll();
} 