package com.slyscrat.impress.service.scheduled.games;

import com.slyscrat.impress.model.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:sources.properties")
public class SteamApiServiceImpl implements SteamApiService {

    @Value("${steam.api.games.list.url2}")
    private String gamesListUrl;
    
    @Value("${steam.api.game.info.url}")
    private String gameInfoUrl;
    
    @Value("${steam.api.key}")
    private String key;

    @Value("${language}")
    private String language;
    
    // private final GameCrudService gameCrudService;
    private final SteamJsonParserService parserService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkGamesList() {
        Set<Integer> dbAppIds = null; // = gameCrudService.getAllIds
        int counter = 0;
        for (Integer gameId: parserService.getGameIdSet(sendRequest(gamesListUrl/*, key*/))) {
            counter++;
            if (dbAppIds == null || !dbAppIds.contains(gameId)) {
                addGameInfo(gameId);
                dbAppIds.add(gameId);
            }
            if (counter > 3) break;
        }

    }

    @Override
    public void addGameInfo(int appId) {
        try {
            GameDto gameDto = parserService.getGameFullDto(sendRequest(gameInfoUrl, language, String.valueOf(appId)));
            // saveDtoTo db
        } catch (IOException ex) {
            //throw new InnerLogicException(SteamApiService.class, "addGameInfo", ex);
        }
    }

    private String sendRequest(String url, String... params) {
        return restTemplate.getForObject(url, String.class, (Object[]) params);
    }
}
