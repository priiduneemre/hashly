package com.neemre.hashly.common.dto.assembly;

import java.util.List;

import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.common.dto.EventDto;

public interface DtoAugmentor {
	
	EventDto augment(EventDto eventDto, EventType eventType, EntityType entityType, Guest guest);
	
	List<EventDto> augment(List<EventDto> eventDtos, List<EventType> eventTypes, 
			List<EntityType> entityTypes, List<Guest> guests);
	
	Event unaugment(Event event, EventDto eventDto);
	
	List<Event> unaugment(List<Event> events, List<EventDto> eventDtos);
}