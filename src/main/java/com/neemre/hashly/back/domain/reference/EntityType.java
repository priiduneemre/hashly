package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EntityType extends ReferenceEntity {

	private int entityTypeId;
	private String code;
	private String label;
}