package com.neemre.hashly.frontend.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.neemre.hashly.backend.data.EntityTypeDao;
import com.neemre.hashly.backend.data.EventDao;
import com.neemre.hashly.backend.data.EventTypeDao;
import com.neemre.hashly.backend.data.GuestDao;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.GuestDto;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.Guest;

@Controller("experimentController")
@RequestMapping("api/v1/experiments")
public class ExperimentController {

	@Autowired
	private EventDao eventDao;
	@Autowired
	private EventTypeDao eventTypeDao;
	@Autowired
	private EntityTypeDao entityTypeDao;
	@Autowired
	private GuestDao guestDao;
	
	
	@ResponseBody
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public EventDto getEvent(@PathVariable(value = "eventId") Long eventId) {
		
		Event outEvent = eventDao.read(eventId);
		System.out.printf("\"%s\": \"%s\";\n", "outEvent", outEvent);
		EventType outEventType = eventTypeDao.read(outEvent.getEventTypeId());
		System.out.printf("\"%s\": \"%s\";\n", "outEventType", outEventType);
		EntityType outEntityType = entityTypeDao.read(outEvent.getEntityTypeId());
		System.out.printf("\"%s\": \"%s\";\n", "outEntityType", outEntityType);
		Guest outGuest = guestDao.read(outEvent.getGuestId());
		System.out.printf("\"%s\": \"%s\";\n", "outGuest", outGuest);
		
		EventDto outEventDto = new EventDto();
		DTOAssembler.newAssembler(EventDto.class, Event.class).assembleDto(outEventDto, outEvent,
				null, null);
		System.out.printf("\"%s\": \"%s\";\n", "outEventDto", outEventDto);
		EventTypeDto outEventTypeDto = new EventTypeDto();
		DTOAssembler.newAssembler(EventTypeDto.class, EventType.class).assembleDto(outEventTypeDto, 
				outEventType, null, null);
		System.out.printf("\"%s\": \"%s\";\n", "outEventTypeDto", outEventTypeDto);
		EntityTypeDto outEntityTypeDto = new EntityTypeDto();
		DTOAssembler.newAssembler(EntityTypeDto.class, EntityType.class).assembleDto(
				outEntityTypeDto, outEntityType, null, null);
		System.out.printf("\"%s\": \"%s\";\n", "outEntityTypeDto", outEntityTypeDto);
		GuestDto outGuestDto = new GuestDto();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleDto(outGuestDto, outGuest,
				null, null);
		System.out.printf("\"%s\": \"%s\";\n", "outGuestDto", outGuestDto);
		outEventDto.setEventType(outEventTypeDto);
		outEventDto.setEntityType(outEntityTypeDto);
		outEventDto.setGuest(outGuestDto);
		System.out.printf("\"%s\" - finalized: \"%s\";\n", "outEventDto", outEventDto);
		return outEventDto;
	}
}