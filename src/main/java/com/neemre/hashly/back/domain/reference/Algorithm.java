package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Algorithm extends ReferenceEntity {
	
	private int algorithmId;
	private String name;
	private String designerName;
	private int digestLengthBits;
	private String description;
}