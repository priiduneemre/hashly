package com.neemre.hashly.common.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class EventDto {

	@DtoField(value = "eventId", readOnly = true)
	private Long eventId;
	private EventTypeDto eventType;
	@DtoField("sourceItemId")
	private Integer sourceItemId;
	private EntityTypeDto entityType;
	private GuestDto guest;
	@DtoField("occurredAt")
	private Date occurredAt;
	
	
	public EventDto() {
		eventType = new EventTypeDto();
		entityType = new EntityTypeDto();
		guest = new GuestDto();
	}
}