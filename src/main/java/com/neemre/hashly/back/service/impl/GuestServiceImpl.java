package com.neemre.hashly.back.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neemre.hashly.back.data.GuestDao;
import com.neemre.hashly.back.domain.Guest;
import com.neemre.hashly.back.service.GuestService;

@Service("guestService")
public class GuestServiceImpl implements GuestService {
	
	@Autowired
	private GuestDao guestDao;


	@Override
	public Guest findById(int guestId) {
		
		return guestDao.read(guestId);
	}

	@Override
	public List<Guest> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guest save(Guest entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int entityId) {
		// TODO Auto-generated method stub
		
	}
}