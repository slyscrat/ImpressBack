package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.model.entity.BookEntity;
import com.slyscrat.impress.model.entity.BookTagEntity;
import com.slyscrat.impress.model.repository.book.BookTagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookMapper extends AbstractMapper<BookEntity, BookDto> {

    private final BookTagRepository bookTagRepository;
    StringBuilder stringBuilder;

    @Autowired
    public BookMapper(BookTagRepository bookTagRepository,
                      ModelMapper modelMapper) {
        super(modelMapper);
        this.bookTagRepository = bookTagRepository;
        stringBuilder = new StringBuilder();
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(BookEntity::setTags);
                    m.skip(BookEntity::setRatedBy);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> m.skip(BookDto::setTags))
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(BookEntity source, BookDto destination) {
        destination.setTags(source.getTags()
                .stream()
                .map(tag -> {
                    BookTagDto dto = new BookTagDto();
                    dto.setId(tag.getId());
                    dto.setName(tag.getName());
                    return dto;
                })
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(BookDto source, BookEntity destination) {
        destination.getTags().forEach(bookTagEntity -> bookTagEntity.getBooks().remove(destination));
        destination.getTags().clear();
        source.getTags().forEach(tag -> {
            BookTagEntity tagEntity = bookTagRepository.findById(tag.getId())
                    .orElseThrow(() -> new EntityNotFoundException(BookTagEntity.class, tag));
            tagEntity.getBooks().add(destination);
            destination.getTags().add(tagEntity);
        });
    }
}

