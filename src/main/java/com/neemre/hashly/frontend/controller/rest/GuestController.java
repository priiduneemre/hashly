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
import org.springframework.web.bind.annotation.ResponseBody;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.domain.reference.enums.EntityTypes;
import com.neemre.hashly.backend.domain.reference.enums.EventTypes;
import com.neemre.hashly.backend.service.EventService;
import com.neemre.hashly.backend.service.GuestService;
import com.neemre.hashly.common.dto.GuestDto;

@Controller("guestController")
@RequestMapping("api/v1/guests")
public class GuestController {

	@Autowired
	private GuestService guestService;
	@Autowired
	private EventService eventService;
	

	@ResponseBody
	@RequestMapping(value = "/{guestId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public GuestDto getGuest(@PathVariable(value = "guestId") int guestId) {
		Guest outGuest = guestService.findById(guestId);
		GuestDto outGuestDto = new GuestDto();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleDto(outGuestDto, outGuest, 
				null, null);
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public List<GuestDto> getAllGuests() {
		List<Guest> outGuests = guestService.findAll();
		List<GuestDto> outGuestDtos = new ArrayList<GuestDto>();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleDtos(outGuestDtos, 
				outGuests, null, null);
		return outGuestDtos;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public GuestDto addNewGuest(HttpServletRequest request, @RequestBody GuestDto inGuestDto) {
		Guest inGuest = new Guest();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleEntity(inGuestDto, 
				inGuest, null, null);
		inGuest.setIpAddress(request.getRemoteAddr());
		Guest outGuest = guestService.addNewGuest(inGuest);
		eventService.addNewEvent(EventTypes.CREATED, outGuest.getGuestId(), EntityTypes.GUEST, 
				request.getRemoteAddr());
		GuestDto outGuestDto = new GuestDto();
		DTOAssembler.newAssembler(GuestDto.class, Guest.class).assembleDto(outGuestDto, outGuest, 
				null, null);
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{guestId}/visitCount", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
	public void updateGuestVisitCount(HttpServletRequest request, 
			@PathVariable(value = "guestId") int guestId, @RequestBody int incrementBy) {
		guestService.updateGuestVisitCount(guestId, incrementBy);
		eventService.addNewEvent(EventTypes.UPDATED, guestId, EntityTypes.GUEST, 
				request.getRemoteAddr());
	}
}