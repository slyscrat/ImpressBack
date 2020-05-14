package com.slyscrat.impress.service.scheduled.book;

import com.slyscrat.impress.model.dto.book.BookDto;
import com.slyscrat.impress.model.dto.book.BookTagDto;

import java.util.Set;

public interface LibraryThingJsonParser {
    Set<BookTagDto> getBookTagsSet(String json);
    Set<BookDto> getBooksSet(String json, Set<BookTagDto> tags);
}
