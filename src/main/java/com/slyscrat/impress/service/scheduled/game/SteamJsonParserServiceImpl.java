package com.slyscrat.impress.service.scheduled.game;

import com.jayway.jsonpath.Configuration;

import com.jayway.jsonpath.JsonPath;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.service.crud.game.GameGenreCrudService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SteamJsonParserServiceImpl implements SteamJsonParserService{

    private final GameGenreCrudService gameGenreCrudService;
    /*private final Set<GameGenreDto> existedGameGenres;

    public SteamJsonParserServiceImpl(GameGenreCrudService gameGenreCrudService){
        this.existedGameGenres = gameGenreCrudService.findAll();
    }*/

    @Override
    public Set<Integer> getGameIdSet(String json) {
        return ((JSONArray) JsonPath.read(json, "$..apps[*].appid")).stream()
                .map(genre -> Integer.parseInt(genre.toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public GameDto getGameFullDto(String json) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Set<GameGenreDto> set = gameGenreCrudService.findAll();
        System.out.println(json);
        GameDto gameDto = new GameDto();
        if (valueOf(document, "$..data.type").toString().equals("dlc")) return null;

        gameDto.setId(Integer.parseInt(valueOf(document, "$..data.steam_appid")));
        gameDto.setName(valueOf(document, "$..data.name"));
        gameDto.setDescription(valueOf(document, "$..data.short_description"));
        gameDto.setIcon(valueOf(document, "$..data.header_image"));
        gameDto.setDeveloper(valueOf(document, "$..data.developers[0]"));

        JSONArray genresIds = JsonPath.read(document, "$..data.genres[*].id");
        gameDto.setGenres(genresIds.stream()
                                .map(genre -> {
                                    int id = Integer.parseInt(genre.toString());
                                    for (GameGenreDto existedGameGenre : set) {
                                        if (existedGameGenre.equals(id))
                                            return existedGameGenre;
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet()));

        JSONArray screens = JsonPath.read(document, "$..data.screenshots[:4].path_thumbnail");
        gameDto.setScreenshots(screens.stream()
                .map(Object::toString)
                .collect(Collectors.toSet()));

        return gameDto;
    }


    private String valueOf(Object document, String path) {
        String res = "";
        try {
            res = ((JSONArray) JsonPath.read(document, path)).get(0).toString();
        }
        catch(IndexOutOfBoundsException ignored){}
        return res;
    }
}
