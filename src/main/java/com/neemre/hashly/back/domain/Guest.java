package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Guest extends Entity {

	private int guestId;
	private String ipAddress;
	private int visitCount;
}