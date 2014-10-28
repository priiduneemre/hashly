package com.neemre.hashly.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.*;

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
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.assembly.DtoAssembler;
import com.neemre.hashly.common.dto.assembly.DtoAugmentor;
import com.neerme.hashly.common.ExceptionMessage;

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

	@Autowired
	private DtoAssembler dtoAssembler;
	@Autowired
	private DtoAugmentor dtoAugmentor;


	@Transactional
	@Override
	public EventDto findById(Long eventId) {
		Event outEvent = eventDao.read(eventId);
		EventDto outEventDto = dtoAssembler.assemble(outEvent, Event.class, new EventDto(), 
				EventDto.class);
		outEventDto = dtoAugmentor.augment(outEventDto, eventTypeDao.read(
				outEvent.getEventTypeId()), entityTypeDao.read(outEvent.getEntityTypeId()), 
				guestDao.read(outEvent.getGuestId()));
		return outEventDto;
	}

	@Transactional
	@Override
	public List<EventDto> findAll() {
		List<Event> outEvents = eventDao.readAll();
		List<EventDto> outEventDtos = dtoAssembler.assemble(outEvents, Event.class, 
				new ArrayList<EventDto>(), EventDto.class);
		for(int i = 0; i < outEventDtos.size(); i++) {
			Event outEvent = outEvents.get(i);
			EventDto outEventDto = outEventDtos.get(i);
			outEventDtos.set(i, dtoAugmentor.augment(outEventDto, eventTypeDao.read(
					outEvent.getEventTypeId()), entityTypeDao.read(outEvent.getEntityTypeId()),
					guestDao.read(outEvent.getGuestId())));
		}
		return outEventDtos;
	}

	@Transactional
	@Override
	public EventDto addNewEvent(EventTypes eventType, Long sourceItemId, EntityTypes entityType,
			String ipAddress) {
		Event event = new Event();
		event.setEventTypeId(findEventTypeByCode(eventType.name()).getEventTypeId());
		event.setSourceItemId(sourceItemId);
		event.setEntityTypeId(findEntityTypeByCode(entityType.name()).getEntityTypeId());
		event.setGuestId(guestDao.readByIpAddress(ipAddress).getGuestId());
		long newEventId = eventDao.create(event);
		Event newEvent = eventDao.read(newEventId);
		EventDto outEventDto = dtoAssembler.assemble(newEvent, Event.class, new EventDto(), 
				EventDto.class);
		outEventDto = dtoAugmentor.augment(outEventDto, eventTypeDao.read(
				newEvent.getEventTypeId()), entityTypeDao.read(newEvent.getEntityTypeId()),
				guestDao.read(newEvent.getGuestId()));
		return outEventDto;
	}

	@Transactional
	@Override
	public List<EventTypeDto> findAllEventTypes() {
		List<EventTypeDto> outEventTypeDtos = dtoAssembler.assemble(eventTypeDao.readAll(), 
				EventType.class, new ArrayList<EventTypeDto>(), EventTypeDto.class);
		return outEventTypeDtos;
	}

	@Transactional
	@Override
	public EventTypeDto findEventTypeByCode(String code) {
		EventTypeDto outEventTypeDto = null;
		List<EventTypeDto> eventTypeDtos = findAllEventTypes();
		for(EventTypeDto eventTypeDto : eventTypeDtos) {
			if(eventTypeDto.getCode().equals(code)) {
				outEventTypeDto = eventTypeDto;
				break;
			}
		}
		checkState(outEventTypeDto != null, ExceptionMessage.METHOD_UNEXPECTED_NULL_RESULT, 
				new Object(){}.getClass().getEnclosingClass().getName());
		return outEventTypeDto;
	}

	@Transactional
	@Override
	public List<EntityTypeDto> findAllEntityTypes() {
		List<EntityTypeDto> outEntityTypeDtos = dtoAssembler.assemble(entityTypeDao.readAll(),
				EntityType.class, new ArrayList<EntityTypeDto>(), EntityTypeDto.class);
		return outEntityTypeDtos;
	}

	@Transactional
	@Override
	public EntityTypeDto findEntityTypeByCode(String code) {
		EntityTypeDto outEntityTypeDto = null;
		List<EntityTypeDto> entityTypeDtos = findAllEntityTypes();
		for(EntityTypeDto entityTypeDto : entityTypeDtos) {
			if(entityTypeDto.getCode().equals(code)) {
				outEntityTypeDto = entityTypeDto;
				break;
			}
		}
		checkState(outEntityTypeDto != null, ExceptionMessage.METHOD_UNEXPECTED_NULL_RESULT,
				new Object(){}.getClass().getEnclosingClass().getName());
		return outEntityTypeDto;
	} 
}