package com.neemre.hashly.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neemre.hashly.backend.data.GuestDao;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.service.EventService;
import com.neemre.hashly.backend.service.GuestService;

@Service("guestService")
public class GuestServiceImpl implements GuestService {
	
	@Autowired
	private GuestDao guestDao;
	@Autowired
	private EventService eventService;

	
	@Override
	public Guest findById(Integer guestId) {
		return guestDao.read(guestId);
	}

	@Override
	public List<Guest> findAll() {
		return guestDao.readAll();
	}
	
	@Override
	public Guest addNewGuest(Guest guest) {
		int newGuestId = guestDao.create(guest);
		Guest newGuest = guestDao.read(newGuestId);
		return newGuest;
	}

	@Override
	public Guest updateGuestVisitCount(int guestId, int incrementBy) {
		Guest updatedGuest = guestDao.read(guestId);
		updatedGuest.setVisitCount(updatedGuest.getVisitCount() + incrementBy);
		guestDao.update(updatedGuest);
		return guestDao.read(guestId);
	}

	@Override
	public void deleteGuest(int guestId) {
		guestDao.delete(guestId);
	}
}