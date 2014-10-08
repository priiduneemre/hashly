package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SourceText extends Entity {

	private int sourceTextId;
	private String contents;
}