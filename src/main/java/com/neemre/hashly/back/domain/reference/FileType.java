package com.neemre.hashly.back.domain.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class FileType extends ReferenceEntity {
	
	private Integer fileTypeId;
	private String extension;
	private String label;
}