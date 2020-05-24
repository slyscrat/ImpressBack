package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.service.crud.CrudService;


public interface MovieRateCrudService extends CrudService<ItemRateDto> {
    //List<ItemShortDto> getShortInfoByUserId(Integer userId);
    ItemRateDto findByUserAndMovie(Integer userId, Integer movieId);
    void delete(Integer userId, Integer movieId);
}
