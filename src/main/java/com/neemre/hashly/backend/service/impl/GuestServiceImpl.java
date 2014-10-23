//package com.neemre.hashly.backend.service.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.neemre.hashly.backend.data.GuestDao;
//import com.neemre.hashly.backend.service.GuestService;
//import com.neemre.hashly.common.dto.GuestDto;
//
//@Service("guestService")
//public class GuestServiceImpl implements GuestService {
//	
//	@Autowired
//	private GuestDao guestDao;
//
//	
//	@Transactional
//	@Override
//	public GuestDto findById(Integer guestId) {
//		return guestDao.read(guestId);
//	}
//
//	@Transactional
//	@Override
//	public List<GuestDto> findAll() {
//		return guestDao.readAll();
//	}
//	
//	@Transactional
//	@Override
//	public GuestDto addNewGuest(GuestDto inGuestDto) {
//		int newGuestId = guestDao.create(guest);
//		Guest newGuest = guestDao.read(newGuestId);
//		return newGuest;
//	}
//
//	@Transactional
//	@Override
//	public GuestDto updateGuestVisitCount(Integer guestId, Integer incrementBy) {
//		Guest updatedGuest = guestDao.read(guestId);
//		updatedGuest.setVisitCount(updatedGuest.getVisitCount() + incrementBy);
//		guestDao.update(updatedGuest);
//		return guestDao.read(guestId);
//	}
//}