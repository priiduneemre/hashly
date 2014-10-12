package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.Algorithm;

public interface AlgorithmDao {
	
	Algorithm read(int algorithmId);

	List<Algorithm> readAll();
}