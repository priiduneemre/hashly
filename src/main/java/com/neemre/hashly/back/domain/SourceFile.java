package com.neemre.hashly.back.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SourceFile extends Entity {
	
	private Integer sourceFileId;
	private Integer fileTypeId;
	private String filename;
	private Long sizeBytes;
}