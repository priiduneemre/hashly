package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.SourceText;

public interface SourceTextDao {
	
	int create(SourceText sourceText);
	
	SourceText read(int sourceTextId);
	
	List<SourceText> readAll();
	
	void update(SourceText sourceText);
	
	void delete(int sourceTextId);
}