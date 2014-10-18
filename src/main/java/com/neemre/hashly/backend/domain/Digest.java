package com.neemre.hashly.backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Digest extends Entity {
	
	private Integer digestId;
	private Integer	algorithmId;
	private Integer resultBundleId;
	private String hexValue;	
}