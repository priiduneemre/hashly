package com.neemre.hashly.common.dto.assembler;

import java.util.List;

import com.neemre.hashly.backend.domain.Guest;
import com.neemre.hashly.backend.domain.reference.EntityType;
import com.neemre.hashly.backend.domain.reference.EventType;
import com.neemre.hashly.backend.domain.Event;
import com.neemre.hashly.common.dto.EntityTypeDto;
import com.neemre.hashly.common.dto.EventDto;
import com.neemre.hashly.common.dto.EventTypeDto;
import com.neemre.hashly.common.dto.GuestDto;

public interface DtoAssembler {
	
	GuestDto assemble(Guest guest);
	
	Guest disassemble(GuestDto guestDto);
	
	EventTypeDto assemble(EventType eventType);
		
	EventType disassemble(EventTypeDto eventTypeDto);

	EntityTypeDto assemble(EntityType entityType);
	
	EntityType disassemble(EntityTypeDto entityTypeDto);
	
	EventDto assembleComposite(Event event, EventType eventType, EntityType entityType,
			Guest guest);
	
	Event disassembleComposite(EventDto eventDto);
}