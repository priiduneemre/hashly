package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.FileDigest;

public interface FileDigestDao {

	int create(FileDigest fileDigest);
	
	FileDigest read(int digestId);
	
	List<FileDigest> readAll();
	
	void update(FileDigest fileDigest);
	
	void delete(int digestId);
}