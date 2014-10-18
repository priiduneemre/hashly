package com.neemre.hashly.backend.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EntityType extends ReferenceEntity {

	private Integer entityTypeId;
	private String code;
	private String label;
}