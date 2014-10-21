package com.neemre.hashly.backend.service;

import com.neemre.hashly.backend.domain.Guest;

public interface GuestService extends Service<Guest, Integer> {
	
	Guest addNewGuest(Guest guest);

	Guest updateGuestVisitCount(int guestId, int incrementBy);
}