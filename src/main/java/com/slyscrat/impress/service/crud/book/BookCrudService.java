package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.service.crud.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BookCrudService extends CrudService<BookDto> {
    Set<Integer> getIdsSet();
    List<BookDto> findAll(Pageable paging);
    List<BookDto> findAllByGenre(Integer genreId, Pageable paging);
    List<BookDto> findAllByGenresIds(Set<Integer> ids, Pageable paging);
}
