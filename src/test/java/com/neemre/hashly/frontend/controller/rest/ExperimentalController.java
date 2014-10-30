package com.neemre.hashly.frontend.controller.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neemre.hashly.backend.domain.reference.enums.EntityTypes;
import com.neemre.hashly.backend.domain.reference.enums.EventTypes;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.util.StringUtils;
import com.neemre.hashly.backend.service.EventService;
import com.neemre.hashly.backend.service.GuestService;

@Controller("experimentalController")
@RequestMapping("api/v1/experimental")
public class ExperimentalController {

	@Autowired
	private GuestService guestService;
	@Autowired
	private EventService eventService;
	
	@ResponseBody
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET, 
			consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public EventDto getEvent(@PathVariable(value = "eventId") Long eventId) {
		EventDto outEventDto = eventService.findById(eventId);
		
		return outEventDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "/events", method = RequestMethod.GET, 
			consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public List<EventDto> getAllEvents() {
		List<EventDto> outEventDtos = eventService.findAll();
		return outEventDtos;
	}
	
	@ResponseBody
	@RequestMapping(value = "/events", method = RequestMethod.POST, 
			consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public EventDto addNewEvent(HttpServletRequest request, @RequestBody Object[] srcItems) {
		EventDto outEventDto = eventService.addNewEvent(EventTypes.valueOf(srcItems[0].toString()),
				Long.parseLong(srcItems[1].toString()), EntityTypes.valueOf(srcItems[2].toString()),
				request.getRemoteAddr());
		return outEventDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "/event-types", method = RequestMethod.GET, 
			consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public List<EventTypeDto> getAllEventTypes(
			@RequestParam(value = "code", required = false) String code) {
		List<EventTypeDto> outEventTypeDtos = new ArrayList<EventTypeDto>();
		if(!new StringUtils().isBlank(code)) {
			outEventTypeDtos.add(eventService.findEventTypeByCode(code));
		} else {
			outEventTypeDtos = eventService.findAllEventTypes();
		}
		return outEventTypeDtos;
	}
	
	@ResponseBody
	@RequestMapping(value = "/entity-types", method = RequestMethod.GET, 
			consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public List<EntityTypeDto> getAllEntityTypes(
			@RequestParam(value = "code", required = false) String code) {
		List<EntityTypeDto> outEntityTypeDtos = new ArrayList<EntityTypeDto>();
		if(!new StringUtils().isBlank(code)) {
			outEntityTypeDtos.add(eventService.findEntityTypeByCode(code));
		} else {
			outEntityTypeDtos = eventService.findAllEntityTypes();
		}
		return outEntityTypeDtos;
	}
}