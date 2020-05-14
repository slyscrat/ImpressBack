package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.book.BookTagDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.Set;

public interface BookTagCrudService extends CrudService<BookTagDto> {
    Set<BookTagDto> getTagsSet();
    BookTagDto getBookTagByName(String name);
}
