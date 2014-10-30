package com.neemre.hashly.common.dto.assembly;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.*;

import com.neemre.hashly.common.misc.ResourceWrapper;
import com.neemre.hashly.common.util.ObjectUtils;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.GuestDto;
import com.neerme.hashly.common.ErrorCodes;

@Component("dtoAugmentor")
public class DtoAugmentorImpl implements DtoAugmentor {

	@Autowired
	private DtoAssembler dtoAssembler;

	@Autowired
	private ResourceWrapper resources;
	

	@Override
	public EventDto augment(EventDto eventDto, EventType eventType, EntityType entityType, 
			Guest guest) {
		eventDto.setEventType(dtoAssembler.assemble(eventType, EventType.class, new EventTypeDto(),
				EventTypeDto.class));
		eventDto.setEntityType(dtoAssembler.assemble(entityType, EntityType.class, 
				new EntityTypeDto(), EntityTypeDto.class));
		eventDto.setGuest(dtoAssembler.assemble(guest, Guest.class, new GuestDto(), 
				GuestDto.class));
		return eventDto;
	}

	@Override
	public List<EventDto> augment(List<EventDto> eventDtos, List<EventType> eventTypes, 
			List<EntityType> entityTypes, List<Guest> guests) {
		String errorMsg = resources.getErrorMessage(ErrorCodes.ARGUMENTS_INCOMPATIBLE_ITEM_COUNT,
				new Object[]{"eventDtos, eventTypes, entityTypes, guests"});
		checkArgument(new ObjectUtils().allEqual(eventDtos.size(), eventTypes.size(), 
				entityTypes.size(), guests.size()), errorMsg); 				
		for(int i = 0; i < eventDtos.size(); i++) {
			eventDtos.set(i, augment(eventDtos.get(i), eventTypes.get(i), entityTypes.get(i), 
					guests.get(i)));
		}
		return eventDtos;
	}

	@Override
	public Event unaugment(Event event, EventDto eventDto) {
		event.setEventTypeId(eventDto.getEventType().getEventTypeId());
		event.setEntityTypeId(eventDto.getEntityType().getEntityTypeId());
		event.setGuestId(eventDto.getGuest().getGuestId());
		return event;
	}

	@Override
	public List<Event> unaugment(List<Event> events, List<EventDto> eventDtos) {
		String errorMsg = resources.getErrorMessage(ErrorCodes.ARGUMENTS_INCOMPATIBLE_ITEM_COUNT,
				new Object[]{"events, eventDtos"});
		checkArgument(new ObjectUtils().allEqual(events.size(), eventDtos.size()), errorMsg);
		for(int i = 0; i < events.size(); i++) {
			events.set(i, unaugment(events.get(i), eventDtos.get(i)));
		}
		return events;
	}
}