package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.FileType;

public interface FileTypeDao {

	FileType read(int fileTypeId);
	
	List<FileType> readAll();	
}