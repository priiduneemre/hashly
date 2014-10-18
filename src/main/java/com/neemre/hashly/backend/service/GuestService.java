package com.neemre.hashly.backend.service;

import java.util.List;

import com.neemre.hashly.backend.domain.Guest;

public interface GuestService {
	
	Guest findById(int guestId);
	
	List<Guest> findAll();
	
	Guest addNewGuest(Guest guest);

	Guest updateGuestVisitCount(int guestId, int incrementBy);
	
	void deleteGuest(int guestId);
}