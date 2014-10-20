package com.neemre.hashly.backend.domain.reference.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum EventTypes {

	CREATED("Item created"),
	READ("Item read"),
	UPDATED("Item updated"),
	DELETED("Item deleted"),
	WEBAPP_REQUESTED("Guest session started"),
	PERMALINK_REQUESTED("Permalink viewed");
	
	@Getter
	@Setter
	private String label;
}