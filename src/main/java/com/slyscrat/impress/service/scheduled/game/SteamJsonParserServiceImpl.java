package com.slyscrat.impress.service.scheduled.game;

import com.jayway.jsonpath.Configuration;

import com.jayway.jsonpath.JsonPath;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.service.crud.game.GameGenreCrudService;
import com.slyscrat.impress.service.scheduled.AbstractJsonParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SteamJsonParserServiceImpl extends AbstractJsonParser implements SteamJsonParserService{

    private final GameGenreCrudService gameGenreCrudService;
    private final Set<GameGenreDto> set = new HashSet<>();

    @Override
    public Set<Integer> getGameIdSet(String json) {
        return ((JSONArray) JsonPath.read(json, "$..apps[*].appid")).stream()
                .map(genre -> Integer.parseInt(genre.toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public GameDto getGameFullDto(String json) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        if (set.isEmpty()) set.addAll(gameGenreCrudService.findAll());
        GameDto gameDto = new GameDto();
        if (valueOf(document, "$..success").equals("false")) return null;
        if (valueOf(document, "$..data.type").equals("dlc")) return null;
        gameDto.setId(Integer.parseInt(valueOf(document, "$..data.steam_appid")));
        gameDto.setName(valueOf(document, "$..data.name"));
        gameDto.setDescription(valueOf(document, "$..data.short_description"));
        if (gameDto.getDescription() == null) return null;
        gameDto.setIcon(valueOf(document, "$..data.header_image"));
        gameDto.setDeveloper(valueOf(document, "$..data.developers[0]"));
        if (gameDto.getDeveloper() == null) return null;
        JSONArray genresIds = JsonPath.read(document, "$..data.genres[*].id");
        gameDto.setGenres(genresIds.stream()
                                .map(genre -> {
                                    int id = Integer.parseInt(genre.toString());
                                    for (GameGenreDto existedGameGenre : set) {
                                        if (existedGameGenre.getId().equals(id))
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
}
