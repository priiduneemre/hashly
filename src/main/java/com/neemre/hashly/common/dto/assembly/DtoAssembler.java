package com.neemre.hashly.common.dto.assembly;

import java.util.List;

public interface DtoAssembler {

	<T, S> S assemble(T entity, Class<T> entityClazz, S dto, Class<S> dtoClazz);

	<T, S> List<S> assemble(List<T> entities, Class<T> entityClazz, List<S> dtos, 
			Class<S> dtoClazz);

	<T, S> T disassemble(S dto, Class<S> dtoClazz, T entity, Class<T> entityClazz);

	<T, S> List<T> disassemble(List<S> dtos, Class<S> dtoClazz, List<T> entities, 
			Class<T> entityClazz);
}