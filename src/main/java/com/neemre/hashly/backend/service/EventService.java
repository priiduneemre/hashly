package com.neemre.hashly.backend.service;

import java.util.List;

import com.neemre.hashly.backend.domain.reference.enums.EntityTypes;
import com.neemre.hashly.backend.domain.reference.enums.EventTypes;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;

public interface EventService extends Service<EventDto, Long> {

	EventDto addNewEvent(EventTypes eventType, Long sourceItemId, EntityTypes entityType, 
			String ipAddress);

	List<EventTypeDto> findAllEventTypes();
	
	EventTypeDto findEventTypeByCode(String code);
	
	List<EntityTypeDto> findAllEntityTypes();
	
	EntityTypeDto findEntityTypeByCode(String code);	
}