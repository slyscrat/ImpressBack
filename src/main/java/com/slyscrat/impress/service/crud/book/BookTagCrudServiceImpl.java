package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.model.entity.BookTagEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.book.BookTagRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookTagCrudServiceImpl
        extends AbstractCrudService<BookTagEntity, BookTagDto, BookTagRepository>
        implements BookTagCrudService {

    @Autowired
    public BookTagCrudServiceImpl(BookTagRepository repository,
                                    Mapper<BookTagEntity, BookTagDto> mapper) {
        super(repository, mapper);
    }


    @Override
    public BookTagDto create(BookTagDto dto) {
        BookTagEntity bookTagEntity = new BookTagEntity();
        mapper.map(dto, bookTagEntity);
        return mapper.map(repository.save(bookTagEntity));
    }

    @Override
    public BookTagDto update(BookTagDto dto) {
        BookTagEntity bookTagEntity = repository.findById(dto.getId()).orElse(new BookTagEntity());
        mapper.map(dto, bookTagEntity);
        return mapper.map(repository.save(bookTagEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<BookTagDto> getTagsSet() {
        return repository.getAllBookTags()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public BookTagDto getBookTagByName(String name) {
        BookTagEntity bookTagEntity = repository.getBookTagEntityByName(name);
        if (bookTagEntity == null)
            throw new EntityNotFoundException(BookTagEntity.class, name);
        BookTagDto bookTagDto = new BookTagDto();
        mapper.map(bookTagDto, bookTagEntity);
        return bookTagDto;
    }
}
