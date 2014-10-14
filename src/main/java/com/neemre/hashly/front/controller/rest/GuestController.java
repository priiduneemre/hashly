package com.neemre.hashly.front.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.neemre.hashly.back.domain.Guest;
import com.neemre.hashly.back.service.GuestService;
import com.neemre.hashly.common.dto.GuestDto;

@Controller("guestController")
@RequestMapping("api/v1/guests")
public class GuestController {

	@Autowired
	private GuestService guestService;
	

	@ResponseBody
	@RequestMapping(value = "/{guestId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public GuestDto getGuest(@PathVariable(value = "guestId") int guestId) {
		Guest outGuest = guestService.findById(guestId);
		GuestDto outGuestDto = new GuestDto();
		DTOAssembler.newAssembler(outGuestDto.getClass(), outGuest.getClass()).assembleDto(
				outGuestDto, outGuest, null, null);
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public List<GuestDto> getGuests() {
		List<Guest> outGuests = guestService.findAll();
		List<GuestDto> outGuestDtos = new ArrayList<GuestDto>();
		DTOAssembler.newAssembler(outGuestDtos.getClass(), outGuests.getClass()).assembleDtos(
				outGuestDtos, outGuests, null, null);
		return outGuestDtos;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public GuestDto addNewGuest(@RequestBody GuestDto inGuestDto) {
		Guest inGuest = new Guest();
		DTOAssembler.newAssembler(inGuestDto.getClass(), inGuest.getClass()).assembleEntity(
				inGuestDto, inGuest, null, null);
		Guest outGuest = guestService.addNewGuest(inGuest);
		GuestDto outGuestDto = new GuestDto();
		DTOAssembler.newAssembler(outGuestDto.getClass(), outGuest.getClass()).assembleDto(
				outGuestDto, outGuest, null, null);
		return outGuestDto;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{guestId}/visitCount", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
	public void updateGuestVisitCount(@PathVariable(value = "guestId") int guestId, 
			@RequestBody int incrementBy) {
		guestService.updateGuestVisitCount(guestId, incrementBy);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{guestId}", method = RequestMethod.DELETE, produces = "application/json; charset=UTF-8")
	public void deleteGuest(@PathVariable(value = "guestId") int guestId) {
		guestService.deleteGuest(guestId);
	}
}