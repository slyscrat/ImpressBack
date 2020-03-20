package com.slyscrat.impress.service.scheduled.games;

public interface SteamApiService {
    void checkGamesList();
    void addGameInfo(long appId);
}
