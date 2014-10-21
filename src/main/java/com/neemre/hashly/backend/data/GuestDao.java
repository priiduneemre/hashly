package com.neemre.hashly.backend.data;

import com.neemre.hashly.backend.domain.Guest;

public interface GuestDao extends Dao<Guest, Integer> {
	
	Guest readByIpAddress(String ipAddress);
}