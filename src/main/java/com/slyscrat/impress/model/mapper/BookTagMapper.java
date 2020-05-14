package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.model.entity.BookTagEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BookTagMapper extends AbstractMapper<BookTagEntity, BookTagDto> {
    @Autowired
    public BookTagMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(BookTagEntity::setBooks);
                    m.skip(BookTagEntity::setId);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .setPostConverter(convertToDto());
    }
}
