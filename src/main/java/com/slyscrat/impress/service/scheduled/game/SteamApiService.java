package com.slyscrat.impress.service.scheduled.game;

public interface SteamApiService {
    void checkGamesList();
    boolean addGameInfo(int appId);
}
