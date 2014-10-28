package com.neemre.hashly.common.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

@Dto
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EventDto extends EntityDto {

	@DtoField(value = "eventId", readOnly = true)
	private Long eventId;
	private EventTypeDto eventType;
	@DtoField("sourceItemId")
	private Long sourceItemId;
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