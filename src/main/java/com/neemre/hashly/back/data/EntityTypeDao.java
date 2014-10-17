package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.EntityType;

public interface EntityTypeDao {

	int create(EntityType entityType);
	
	EntityType read(int entityTypeId);
	
	List<EntityType> readAll();
	
	void update(EntityType entityType);
	
	void delete(int entityTypeId);
}