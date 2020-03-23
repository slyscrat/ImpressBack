package com.slyscrat.impress.service.scheduled.games;

import com.slyscrat.impress.model.dto.GameDto;

public interface SteamApiService {
    void checkGamesList();
    void addGameInfo(int appId);
}
