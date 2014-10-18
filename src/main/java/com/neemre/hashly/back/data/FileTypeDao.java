package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.FileType;

public interface FileTypeDao {

	int create(FileType fileType);
	
	FileType read(int fileTypeId);
	
	List<FileType> readAll();
	
	void update(FileType fileType);
	
	void delete(int fileTypeId);
}