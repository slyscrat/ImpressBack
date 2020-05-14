package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.Set;

public interface GameCrudService extends CrudService<GameDto> {

    Set<Integer> getIdsSet();
    //Set<GameDto> getAllUserGames();
}
