package com.neemre.hashly.backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ResultBundle extends Entity {
	 
	private Integer resultBundleId;
	private Integer guestId;
	private String permacode;
	private Integer viewCount;
}