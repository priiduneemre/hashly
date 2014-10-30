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
@JsonPropertyOrder({"guestId", "ipAddress", "visitCount"})
public class GuestDto extends EntityDto {

	@DtoField(value = "guestId", readOnly = true)
	private Integer guestId;
	@DtoField("ipAddress")
	private String ipAddress;
	@DtoField("visitCount")
	private Integer visitCount;
}