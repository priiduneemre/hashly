package com.neemre.hashly.common.dto.assembler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.neemre.hashly.backend.domain.Entity;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.common.dto.EntityDto;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.GuestDto;

@Component("dtoAssembler")
public class DtoAssemblerImpl implements DtoAssembler {
	

	
	@Override
	public GuestDto assemble(Guest guest) {
		GuestDto guestDto = new GuestDto();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleDto(guestDto, guest, null,
				null);
		return guestDto;
	}
	
	@Override
	public Guest disassemble(GuestDto guestDto) {
		Guest guest = new Guest();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleEntity(guestDto, guest,
				null, null);
		return guest;
	}
	
	@Override
	public EventTypeDto assemble(EventType eventType) {
		EventTypeDto eventTypeDto = new EventTypeDto();
		DTOAssembler.newAssembler(EventTypeDto.class, EventType.class).assembleDto(eventTypeDto,
				eventType, null, null);
		
		return eventTypeDto;
	}
	
	@Override
	public EventType disassemble(EventTypeDto eventTypeDto) {
		EventType eventType = new EventType();
		DTOAssembler.newAssembler(EventTypeDto.class, EventType.class).assembleEntity(eventTypeDto,
				eventType, null, null);
		return eventType;
	}
	
	@Override
	public EntityTypeDto assemble(EntityType entityType) {
		EntityTypeDto entityTypeDto = new EntityTypeDto();
		DTOAssembler.newAssembler(EntityTypeDto.class, EntityType.class).assembleDto(entityTypeDto,
				entityType, null, null);
		return entityTypeDto;
	}
	
	@Override
	public EntityType disassemble(EntityTypeDto entityTypeDto) {
		EntityType entityType = new EntityType();
		DTOAssembler.newAssembler(EntityTypeDto.class, EntityType.class).assembleEntity(
				entityTypeDto, entityType, null, null);
		return entityType;
	}
	
	@Override
	public EventDto assembleComposite(Event event, EventType eventType, EntityType entityType, 
			Guest guest) {
		EventDto eventDto = new EventDto();
		assemble(event);
		assemble(eventType);
		assemble(entityType);
		assemble(guest);
		return eventDto;
	}
	
	@Override
	public Event disassembleComposite(EventDto eventDto) {
		Event event = new Event();
		disassemble(eventDto);
		event.setEventTypeId(eventDto.getEventType().getEventTypeId());
		event.setEntityTypeId(eventDto.getEntityType().getEntityTypeId());
		event.setGuestId(eventDto.getGuest().getGuestId());
		return event;
	}
	
	private EventDto assemble(Event event) {
		EventDto eventDto = new EventDto();
		DTOAssembler.newAssembler(EventDto.class, Event.class).assembleDto(eventDto, event, null, 
				null);
		return eventDto;
	}
	
	private Event disassemble(EventDto eventDto) {
		Event event = new Event();
		DTOAssembler.newAssembler(EventDto.class, Event.class).assembleEntity(eventDto, event,
				null, null);
		return event;
	}
}