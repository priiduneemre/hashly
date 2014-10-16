package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.SourceFile;

public interface SourceFileDao {

	int create(SourceFile sourceFile);
	
	SourceFile read(int sourceFileId);
	
	List<SourceFile> readAll();
	
	void update(SourceFile sourceFile);
	
	void delete(int sourceFileId);
}