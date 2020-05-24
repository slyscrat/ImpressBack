package com.slyscrat.impress.service.business.game;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;

import java.util.List;
import java.util.Set;

public interface GameService {

    List<GameDto> getRecommendedList(Integer page, Integer userId);
    List<GameDto> getFutureList(Integer page, Integer userId);
    List<GameDto> getRatedList(Integer page, Integer userId);
    List<GameDto> getAllList(Integer page, Integer sort, Set<Integer> genres, Integer userId);
    List<GameDto> getByNameList(Integer page, String name, Integer userId);

    GameDto getById(Integer id, Integer userId);
    ItemRateDto rate(Integer gameId, Short rate, Integer userId);
    ItemRateDto note(Integer gameId, String note, Integer userId);

    List<GameGenreDto> getGenres();
    void noteDel(Integer gameId, Integer userId);
}
