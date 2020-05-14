package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.entity.BookEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.book.BookRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class BookCrudServiceImpl
        extends AbstractCrudService<BookEntity, BookDto, BookRepository>
        implements BookCrudService {

    @Autowired
    public BookCrudServiceImpl(BookRepository repository,
                               Mapper<BookEntity, BookDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getIdsSet();
    }

    @Override
    public BookDto create(BookDto dto) {
        BookEntity bookEntity = new BookEntity();
        mapper.map(dto, bookEntity);
        return mapper.map(repository.save(bookEntity));
    }

    @Override
    public BookDto update(BookDto dto) {
        BookEntity bookEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(BookEntity.class, dto.getId()));
        mapper.map(dto, bookEntity);
        return mapper.map(repository.save(bookEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
