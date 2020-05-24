package com.slyscrat.impress.service.crud.book;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.service.crud.CrudService;


public interface BookRateCrudService extends CrudService<ItemRateDto> {
    ItemRateDto findByUserAndBook(Integer userId, Integer bookId);
    void delete(Integer userId, Integer bookId);
}
