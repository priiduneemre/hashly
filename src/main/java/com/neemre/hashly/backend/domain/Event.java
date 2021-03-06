package com.neemre.hashly.backend.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Event extends Entity {

	private Long eventId;
	private Integer eventTypeId;
	private Long sourceItemId;
	private Integer entityTypeId;
	private Integer guestId;
	private Date occurredAt;
}