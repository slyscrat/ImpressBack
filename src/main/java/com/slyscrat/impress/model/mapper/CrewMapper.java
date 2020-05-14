package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.movie.CrewDto;
import com.slyscrat.impress.model.entity.CrewEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CrewMapper extends AbstractMapper<CrewEntity, CrewDto> {

    @Autowired
    public CrewMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .setPostConverter(convertToDto());
    }
}

