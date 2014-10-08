package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SourceFile extends Entity {
	
	private int sourceFileId;
	private int fileTypeId;
	private String filename;
	private long sizeBytes;
}