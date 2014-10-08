package com.neemre.hashly.back.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Event extends Entity {

	private long eventId;
	private int eventTypeId;
	private int sourceItemId;
	private int entityTypeId;
	private int guestId;
	private Date occurredAt;
}