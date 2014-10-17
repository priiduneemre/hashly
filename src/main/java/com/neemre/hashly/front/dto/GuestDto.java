package com.neemre.hashly.common.dto;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Dto
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class GuestDto {

	@DtoField(value = "guestId", readOnly = true)
	private Integer guestId;
	@DtoField("ipAddress")
	private String ipAddress;
	@DtoField("visitCount")
	private Integer visitCount;
}