package com.neemre.hashly.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Dto
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"entityTypeId", "code", "label"})
public class EntityTypeDto extends EntityDto {

	@DtoField(value = "entityTypeId", readOnly = true)
	private Integer entityTypeId;
	@DtoField("code")
	private String code;
	@DtoField("label")
	private String label;
}