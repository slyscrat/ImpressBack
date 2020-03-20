package com.slyscrat.impress.service.scheduled.games;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SteamApiServiceImpl implements SteamApiService {
    private static final String GAMES_LIST_URL = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json";
    private static final String GAME_INFO_URL = "https://store.steampowered.com/api/appdetails?l=russian&appids=";
    // private final GameCrudService gameCrudService;
    private final SteamJsonParserService parserService;
    // TODO: think about DTO
    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkGamesList() {
        String url = GAMES_LIST_URL;
        // send Steam request
        List<Long> appIds = null; // = gameCrudService.getAllIds

        for (Long gameId: parserService.getGameIdSet()) {
            if (appIds != null || !appIds.contains(gameId)) {
                addGameInfo(gameId);
                appIds.add(gameId);
            }
        }
    }

    @Override
    public void addGameInfo(long appId) {
        String url = GAME_INFO_URL + appId;
        // send Steam request
        /*
        parseToDto(result);
        saveDtoTo db
        * */
    }
}
