package com.neemre.hashly.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemre.hashly.backend.data.GuestDao;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.service.GuestService;
import com.neemre.hashly.common.dto.GuestDto;
import com.neemre.hashly.common.dto.assembly.DtoAssembler;

@Service("guestService")
public class GuestServiceImpl implements GuestService {

	@Autowired
	private GuestDao guestDao;

	@Autowired
	private DtoAssembler dtoAssembler;

	
	@Transactional
	@Override
	public GuestDto findById(Integer guestId) {
		GuestDto outGuestDto = dtoAssembler.assemble(guestDao.read(guestId), Guest.class, 
				new GuestDto(), GuestDto.class);
		return outGuestDto;
	}

	@Transactional
	@Override
	public List<GuestDto> findAll() {
		List<GuestDto> outGuestDtos = dtoAssembler.assemble(guestDao.readAll(), Guest.class, 
				new ArrayList<GuestDto>(), GuestDto.class);
		return outGuestDtos;
	}

	@Transactional
	@Override
	public GuestDto addNewGuest(GuestDto inGuestDto) {
		Guest inGuest = dtoAssembler.disassemble(inGuestDto, GuestDto.class, new Guest(), 
				Guest.class);
		int newGuestId = guestDao.create(inGuest);
		Guest newGuest = guestDao.read(newGuestId);
		GuestDto outGuestDto = dtoAssembler.assemble(newGuest, Guest.class, new GuestDto(), 
				GuestDto.class);
		return outGuestDto;
	}

	@Transactional
	@Override
	public GuestDto updateGuestVisitCount(Integer guestId, Integer incrementBy) {
		Guest updatedGuest = guestDao.read(guestId);
		updatedGuest.setVisitCount(updatedGuest.getVisitCount() + incrementBy);
		guestDao.update(updatedGuest);
		GuestDto outGuestDto = dtoAssembler.assemble(guestDao.read(guestId), Guest.class, 
				new GuestDto(), GuestDto.class);
		return outGuestDto;
	}
}