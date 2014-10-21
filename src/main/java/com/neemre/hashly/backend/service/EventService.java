package com.neemre.hashly.backend.service;

import java.util.List;

import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.backend.domain.reference.enums.EntityTypes;
import com.neemre.hashly.backend.domain.reference.enums.EventTypes;

public interface EventService extends Service<Event, Long> {

	Event addNewEvent(EventTypes eventType, int sourceItemId, EntityTypes entityType, 
			String ipAddress);

	List<EventType> findAllEventTypes();
	
	EventType findEventTypeByCode(String code);
	
	List<EntityType> findAllEntityTypes();
	
	EntityType findEntityTypeByCode(String code);	
}