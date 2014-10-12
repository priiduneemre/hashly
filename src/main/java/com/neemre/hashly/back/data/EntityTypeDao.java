package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.EntityType;

public interface EntityTypeDao {

	EntityType read(int entityTypeId);
	
	List<EntityType> readAll();
}