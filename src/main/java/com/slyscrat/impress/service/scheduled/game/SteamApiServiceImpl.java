package com.slyscrat.impress.service.scheduled.game;

import com.slyscrat.impress.exception.InnerLogicException;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.service.crud.game.GameCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    
    @Value("${steam.api.key}")
    private String key;
    
    private final GameCrudService gameCrudService;
    private final SteamJsonParserService parserService;
    private final RestTemplate restTemplate = new RestTemplate();

    // TODO : remove counter
    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkGamesList() {
        System.out.println("-----------------------------------------------------------------");
        gameCrudService.getAllAppIds();
        Set<Integer> dbAppIds = new HashSet();
        int counter = 0;
        String res = sendRequest(gamesListUrl);
        Set<Integer> set = parserService.getGameIdSet(res);
        //for (Integer gameId: parserService.getGameIdSet(sendRequest(gamesListUrl/*, key*/))) {
        for (Integer gameId: set) {
            counter++;
            System.out.println(counter);
            if (dbAppIds == null || !dbAppIds.contains(gameId)) {
                if (counter < 4) continue;
                if (counter == 10) break;
                System.out.println(gameId);
                if(addGameInfo(gameId))
                    dbAppIds.add(gameId);
            }
            //if (counter > 3) break;
        }

    }

    @Override
    public boolean addGameInfo(int appId) {
        String res = sendRequest(gameInfoUrl + appId);
        if (containsHanScript(res)) return false;
        //GameDto gameDto = parserService.getGameFullDto(sendRequest(gameInfoUrl, language, String.valueOf(appId)));
        GameDto gameDto = parserService.getGameFullDto(res);
        if (gameDto == null) return false;
        System.out.println(gameDto);
        int count = 0;
        for (String screen : gameDto.getScreenshots()) {
            count += screen.length();
        }
        System.out.println("des= " + gameDto.getDescription().length());
        System.out.println("dev= " + gameDto.getDeveloper().length());
        System.out.println("ico= " + gameDto.getIcon().length());
        System.out.println("nam= " + gameDto.getName().length());
        gameCrudService.create(gameDto);
        return true;
        /*try {

        } catch (IOException ex) {
            throw new InnerLogicException(SteamApiService.class, "addGameInfo", ex);
        }*/
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
