package com.neemre.hashly.common.dto;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class EventTypeDto {

	@DtoField(value = "eventTypeId", readOnly = true)
	private Integer eventTypeId;
	@DtoField("code")
	private String code;
	@DtoField("label")
	private String label;
}