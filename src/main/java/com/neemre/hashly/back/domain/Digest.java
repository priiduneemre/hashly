package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Digest extends Entity {
	
	private int digestId;
	private int	algorithmId;
	private int resultBundleId;
	private String hexValue;	
}