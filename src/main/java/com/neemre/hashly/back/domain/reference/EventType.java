package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EventType extends ReferenceEntity {

	private Integer eventTypeId;
	private String code;
	private String label;
}