package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.reference.EventType;

public interface EventTypeDao {

	EventType read(int eventTypeId);
	
	List<EventType> readAll();
}