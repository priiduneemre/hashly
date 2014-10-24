package com.neemre.hashly.common.dto.assembly;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

@Component("dtoAssembler")
public class DtoAssemblerImpl implements DtoAssembler {
	
	@Override
	public <T, S> S assemble(T entity, Class<T> entityClazz, S dto, Class<S> dtoClazz) {
		DTOAssembler.newAssembler(dtoClazz, entityClazz).assembleDto(dto, entity, null, null);
		return dto;
	}
	
	@Override
	public <T, S> List<S> assemble(List<T> entities, Class<T> entityClazz, List<S> dtos, 
			Class<S> dtoClazz) {
		DTOAssembler.newAssembler(dtoClazz, entityClazz).assembleDtos(dtos, entities, null, null);
		return dtos;
	}
	
	@Override
	public <T, S> T disassemble(S dto, Class<S> dtoClazz, T entity, Class<T> entityClazz) {
		DTOAssembler.newAssembler(dtoClazz, entityClazz).assembleEntity(dto, entity, null, null);
		return entity;
	}
	
	@Override
	public <T, S> List<T> disassemble(List<S> dtos, Class<S> dtoClazz, List<T> entities, 
			Class<T> entityClazz) {
		DTOAssembler.newAssembler(dtoClazz, entityClazz).assembleEntities(dtos, entities, null,
				null);
		return entities;
	}
}