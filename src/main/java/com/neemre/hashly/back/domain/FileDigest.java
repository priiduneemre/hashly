package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class FileDigest extends Entity {
	
	private int digestId;
	private int sourceFileId;
}