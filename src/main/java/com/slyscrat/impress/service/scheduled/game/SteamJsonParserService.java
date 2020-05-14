package com.slyscrat.impress.service.scheduled.game;

import com.slyscrat.impress.model.dto.game.GameDto;

import java.util.Set;

public interface SteamJsonParserService {
    Set<Integer> getGameIdSet(String json);
    GameDto getGameFullDto(String json);
}
