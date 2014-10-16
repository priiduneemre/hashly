package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.Event;

public interface EventDao {
	
	long create(Event event);
	
	Event read(int eventId);
	
	List<Event> readAll();
	
	void update(Event event);
	
	void delete(int eventId);
}