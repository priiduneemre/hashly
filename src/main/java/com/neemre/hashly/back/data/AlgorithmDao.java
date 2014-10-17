package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.Algorithm;

public interface AlgorithmDao {
	
	int create(Algorithm algorithm);
	
	Algorithm read(int algorithmId);

	List<Algorithm> readAll();

	void update(Algorithm algorithm);
	
	void delete(int algorithmId);
}