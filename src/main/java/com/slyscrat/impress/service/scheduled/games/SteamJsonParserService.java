package com.slyscrat.impress.service.scheduled.games;

import com.slyscrat.impress.model.dto.GameDto;

import java.io.IOException;
import java.util.Set;

public interface SteamJsonParserService {
    Set<Integer> getGameIdSet(String json);
    GameDto getGameFullDto(String json) throws IOException;
}
