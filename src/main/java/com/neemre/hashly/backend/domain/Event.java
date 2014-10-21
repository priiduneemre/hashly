package com.neemre.hashly.backend.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Event extends Entity {

	private Long eventId;
	private Integer eventTypeId;
	private Integer sourceItemId;
	private Integer entityTypeId;
	private Integer guestId;
	private Date occurredAt;
}