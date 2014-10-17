package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.EventType;

public interface EventTypeDao {

	int create(EventType eventType);
	
	EventType read(int eventTypeId);
	
	List<EventType> readAll();

	void update(EventType eventType);
	
	void delete(int eventTypeId);
}