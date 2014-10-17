package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.ResultBundle;

public interface ResultBundleDao {

	int create(ResultBundle resultBundle);
	
	ResultBundle read(int resultBundleId);
	
	List<ResultBundle> readAll();
	
	void update(ResultBundle resultBundle);
	
	void delete(int resultBundleId);
}