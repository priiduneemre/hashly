package com.neemre.hashly.frontend.controller.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		GuestDto outGuestDto = guestService.findById(guestId);
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public List<GuestDto> getAllGuests() {
		List<GuestDto> outGuestDtos = guestService.findAll();
		return outGuestDtos;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public GuestDto addNewGuest(HttpServletRequest request, @RequestBody GuestDto inGuestDto) {
		//inGuestDto.setIpAddress(request.getRemoteAddr());		//Comment out during: DEVELOPMENT-TIME
		GuestDto outGuestDto = guestService.addNewGuest(inGuestDto);
		eventService.addNewEvent(EventTypes.CREATED, outGuestDto.getGuestId().longValue(),
				EntityTypes.GUEST, request.getRemoteAddr());
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{guestId}/visitCount", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
	public void updateGuestVisitCount(HttpServletRequest request, 
			@PathVariable(value = "guestId") Integer guestId, @RequestBody Integer incrementBy) {
		guestService.updateGuestVisitCount(guestId, incrementBy);
		eventService.addNewEvent(EventTypes.UPDATED, guestId.longValue(), EntityTypes.GUEST,
				request.getRemoteAddr());
	}
}