package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.TextDigest;

public interface TextDigestDao {
	
	int create(TextDigest textDigest);
	
	TextDigest read(int digestId);
	
	List<TextDigest> readAll();
	
	void update(TextDigest textDigest);
	
	void delete(int digestId);
}