package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.Digest;

public interface DigestDao {

	int create(Digest digest);

	Digest read(int digestId);
	
	List<Digest> readAll();
	
	void update(Digest digest);
	
	void delete(int digestId);
}