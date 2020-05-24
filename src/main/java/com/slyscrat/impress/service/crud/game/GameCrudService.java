package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.service.crud.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface GameCrudService extends CrudService<GameDto> {

    Set<Integer> getIdsSet();
    List<GameDto> findAll(Pageable paging);
    List<GameDto> findAllByGenre(Integer genreId, Pageable paging);
    List<GameDto> findAllByGenresIds(Set<Integer> ids, Pageable paging);
}
