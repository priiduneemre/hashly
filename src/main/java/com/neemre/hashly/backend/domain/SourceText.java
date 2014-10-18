package com.neemre.hashly.backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SourceText extends Entity {

	private Integer sourceTextId;
	private String contents;
}