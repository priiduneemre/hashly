package com.neemre.hashly.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neemre.hashly.backend.data.EventDao;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.service.EventService;

@Service("eventService")
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	
	@Override
	public Event findById(Long eventId) {
		return eventDao.read(eventId);
	}

	@Override
	public List<Event> findAll() {
		return eventDao.readAll();
	}

	@Override
	public Event addNewEvent(Event event) {
		long newEventId = eventDao.create(event);
		Event newEvent = eventDao.read(newEventId);
		return newEvent;
	}	
}