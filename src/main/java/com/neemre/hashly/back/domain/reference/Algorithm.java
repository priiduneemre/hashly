package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Algorithm extends ReferenceEntity {
	
	private Integer algorithmId;
	private String name;
	private String designerName;
	private Integer digestLengthBits;
	private String description;
}