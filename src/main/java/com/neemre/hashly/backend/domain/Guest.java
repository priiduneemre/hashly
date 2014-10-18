package com.neemre.hashly.backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Guest extends Entity {

	private Integer guestId;
	private String ipAddress;
	private Integer visitCount;
}