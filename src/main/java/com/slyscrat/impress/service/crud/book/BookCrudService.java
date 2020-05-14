package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.Set;

public interface BookCrudService extends CrudService<BookDto> {
    Set<Integer> getIdsSet();
}
