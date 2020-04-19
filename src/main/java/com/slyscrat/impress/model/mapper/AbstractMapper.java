package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.DataTransferObject;
import com.slyscrat.impress.model.entity.AbstractDataBaseEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class AbstractMapper<
        E extends AbstractDataBaseEntity,
        T extends DataTransferObject
        > implements Mapper<E, T> {
    protected final ModelMapper mapper;
    final Class<E> entityClass;
    final Class<T> dtoClass;

    @SuppressWarnings("unchecked")
    protected AbstractMapper(ModelMapper modelMapper) {
        this.mapper = modelMapper;
        this.entityClass = (Class<E>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.dtoClass = (Class<T>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    @Override
    public T map(E source) {
        return Objects.isNull(source) ? null : mapper.map(source, dtoClass);
    }

    @Override
    public void map(T source, E destination) {
        mapper.map(source, destination);
    }

    protected void mapSpecificFields(E source, T destination) {}

    protected void mapSpecificFields(T source, E destination) {}

    Converter<E, T> convertToDto() {
        return context -> {
            E source = context.getSource();
            T destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    Converter<T, E> convertToEntity() {
        return context -> {
            T source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
}
