package com.neemre.hashly.back.data;

import java.util.List;

import com.neemre.hashly.back.domain.Guest;

public interface GuestDao {
	
	int create(Guest guest);
	
	Guest read(int guestId);
	
	List<Guest> readAll();
	
	void update(Guest guest);
	
	void delete(int guestId);
}