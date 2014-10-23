package com.neemre.hashly.backend.service;

import com.neemre.hashly.common.dto.GuestDto;

public interface GuestService extends Service<GuestDto, Integer> {
	
	GuestDto addNewGuest(GuestDto guestDto);

	GuestDto updateGuestVisitCount(Integer guestId, Integer incrementBy);
}