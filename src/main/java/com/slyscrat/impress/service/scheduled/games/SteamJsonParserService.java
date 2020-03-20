package com.slyscrat.impress.service.scheduled.games;

import com.slyscrat.impress.model.dto.GameDto;

import java.util.Set;

public interface SteamJsonParserService {
    Set<Long> getGameIdSet();
    GameDto getGameFullDto();
}
