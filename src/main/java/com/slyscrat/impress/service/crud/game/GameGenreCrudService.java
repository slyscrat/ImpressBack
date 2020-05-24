package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.List;
import java.util.Set;

public interface GameGenreCrudService extends CrudService<GameGenreDto> {
    Set<Integer> getIdsSet();
    List<GameGenreDto> getAll();
}
