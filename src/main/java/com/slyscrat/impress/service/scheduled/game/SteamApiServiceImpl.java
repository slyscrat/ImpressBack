package com.slyscrat.impress.service.scheduled.game;

import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.service.crud.game.GameCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:sources.properties")
public class SteamApiServiceImpl implements SteamApiService {

    @Value("${steam.api.games.list.url2}")
    private String gamesListUrl;
    
    @Value("${steam.api.game.info.url}")
    private String gameInfoUrl;

    private final GameCrudService gameCrudService;
    private final SteamJsonParserService parserService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkNewGames() {
        GameDto game;
        Set<Integer> dbAppIds = new HashSet<>(gameCrudService.getAllAppIds());
        int req = 0;
        for (Integer gameId: parserService.getGameIdSet(sendRequest(gamesListUrl))) {
            if (dbAppIds.isEmpty() || !dbAppIds.contains(gameId)) {
                req++;
                if (req % 5 == 0) try { Thread.sleep(10000); } catch (InterruptedException e) {}

                game = sendAppIdReq(gameId);
                if (game != null && !dbAppIds.contains(game.getId())){
                    gameCrudService.create(game);
                    dbAppIds.add(game.getId());
                }
            }
        }

    }

    private GameDto sendAppIdReq(int appId) {
        String res = sendRequest(gameInfoUrl + appId);
        if (containsHanScript(res)) return null;
        return parserService.getGameFullDto(res);
    }

    private boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

    private String sendRequest(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
