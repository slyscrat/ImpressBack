package com.slyscrat.impress.service.business.book;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.book.BookDto;

import java.util.List;
import java.util.Set;

public interface BookService {

    List<BookDto> getRecommendedList(Integer page, Integer userId);
    List<BookDto> getFutureList(Integer page, Integer userId);
    List<BookDto> getRatedList(Integer page, Integer userId);
    List<BookDto> getAllList(Integer page, Integer sort, Integer userId);
    List<BookDto> getByNameList(Integer page, String name, Integer userId);

    BookDto getById(Integer id, Integer userId);
    ItemRateDto rate(Integer bookId, Short rate, Integer userId);
    ItemRateDto note(Integer bookId, String note, Integer userId);

    void noteDel(Integer bookId, Integer userId);
}
