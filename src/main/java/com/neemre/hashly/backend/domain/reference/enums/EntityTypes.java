package com.neemre.hashly.backend.domain.reference.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum EntityTypes {

	ALGORITHM("Algorithm"),
	GUEST("Guest"),
	FILE_TYPE("File type"),
	SOURCE_TEXT("Source text"),
	SOURCE_FILE("Source file"),
	RESULT_BUNDLE("Result bundle"),
	DIGEST("Digest"),
	TEXT_DIGEST("Text-based digest"),
	FILE_DIGEST("File-based digest"),
	EVENT_TYPE("Event type"),
	ENTITY_TYPE("Entity type"),
	EVENT("Event");
	
	@Getter
	@Setter
	private String label;
}