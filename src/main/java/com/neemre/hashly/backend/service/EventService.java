package com.neemre.hashly.backend.service;

import com.neemre.hashly.backend.domain.Event;

public interface EventService extends Service<Event, Long> {
	
	Event addNewEvent(Event event);
}