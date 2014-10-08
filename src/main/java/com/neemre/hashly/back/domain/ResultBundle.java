package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ResultBundle extends Entity {
	 
	private int resultBundleId;
	private int guestId;
	private String permacode;
	private int viewCount;
}