package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.ItemShortDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.List;
import java.util.Set;


public interface MovieRateCrudService extends CrudService<ItemRateDto> {
    List<ItemShortDto> getShortInfoByUserId(Integer userId);
    ItemRateDto findByUserAndMovie(Integer userId, Integer movieId);
}
