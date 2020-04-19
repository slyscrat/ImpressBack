package com.slyscrat.impress.service.crud;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.DataTransferObject;
import com.slyscrat.impress.model.entity.AbstractDataBaseEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCrudService<
		E extends AbstractDataBaseEntity,
		D extends DataTransferObject,
		R extends JpaRepository<E, Integer>>
		implements CrudService<D> {
	protected final R repository;
	protected final Mapper<E, D> mapper;

	@Override
	public D findById(Integer id) {
		return mapper.map(
				repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id))
		);
	}

	@Override
	public boolean existsById(Integer id) {
		return repository.existsById(id);
	}

	@Override
	public Set<D> findAll(Integer page, Integer size) {
		return repository.findAll(PageRequest.of(page, size))
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<D> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public long count() {
		return repository.count();
	}
}
