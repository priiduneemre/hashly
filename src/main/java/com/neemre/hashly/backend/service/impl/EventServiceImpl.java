package com.neemre.hashly.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemre.hashly.backend.data.EntityTypeDao;
import com.neemre.hashly.backend.data.EventDao;
import com.neemre.hashly.backend.data.EventTypeDao;
import com.neemre.hashly.backend.data.GuestDao;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.enums.EntityTypes;
import com.neemre.hashly.backend.domain.reference.enums.EventTypes;
import com.neemre.hashly.backend.service.EventService;

@Service("eventService")
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;
	@Autowired
	private EventTypeDao eventTypeDao;
	@Autowired
	private EntityTypeDao entityTypeDao;
	@Autowired
	private GuestDao guestDao;
	

	@Transactional
	@Override
	public Event findById(Long eventId) {
		return eventDao.read(eventId);
	}
	
	@Transactional
	@Override
	public List<Event> findAll() {
		return eventDao.readAll();
	}

	@Transactional
	@Override
	public Event addNewEvent(EventTypes eventType, int sourceItemId, EntityTypes entityType,
			String ipAddress) {
		Event event = new Event();
		event.setEventTypeId(findEventTypeByCode(eventType.name()).getEventTypeId());
		event.setSourceItemId(sourceItemId);
		event.setEntityTypeId(findEntityTypeByCode(entityType.name()).getEntityTypeId());
		event.setGuestId(guestDao.readByIpAddress(ipAddress).getGuestId());
		long newEventId = eventDao.create(event);
		Event newEvent = eventDao.read(newEventId);
		return newEvent;
	}
	
	@Transactional
	@Override
	public List<EventType> findAllEventTypes() {
		return eventTypeDao.readAll();
	}

	@Transactional
	@Override
	public EventType findEventTypeByCode(String code) {
		List<EventType> eventTypes = findAllEventTypes();
		for(EventType eventType : eventTypes) {
			if(eventType.getCode().equals(code)) {
				return eventType;
			}
		}
		return null;
	}

	@Transactional
	@Override
	public List<EntityType> findAllEntityTypes() {
		return entityTypeDao.readAll();
	}

	@Transactional
	@Override
	public EntityType findEntityTypeByCode(String code) {
		List<EntityType> entityTypes = findAllEntityTypes();
		for(EntityType entityType : entityTypes) {
			if(entityType.getCode().equals(code)) {
				return entityType;
			}
		}
		return null;
	} 
}