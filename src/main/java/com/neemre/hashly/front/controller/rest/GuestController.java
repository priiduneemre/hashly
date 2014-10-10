package com.neemre.hashly.front.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neemre.hashly.back.domain.Guest;
import com.neemre.hashly.back.service.GuestService;

@Controller
@RequestMapping("api/v1/guest")
public class GuestController {

	@Autowired
	private GuestService guestService;
	
	
	@ResponseBody
	@RequestMapping(value = "/{guestId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Guest getGuest(@PathVariable(value = "guestId") int guestId) {
		Guest requestedGuest = guestService.findById(guestId);
		return requestedGuest;
	}
}