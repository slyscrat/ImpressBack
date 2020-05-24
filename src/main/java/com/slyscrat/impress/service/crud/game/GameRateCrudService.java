package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.service.crud.CrudService;


public interface GameRateCrudService extends CrudService<ItemRateDto> {
    ItemRateDto findByUserAndGame(Integer userId, Integer gameId);
    void delete(Integer userId, Integer gameId);
}
