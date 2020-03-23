package com.slyscrat.impress.service.scheduled.games;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.slyscrat.impress.model.dto.GameDto;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SteamJsonParserServiceImpl implements SteamJsonParserService{
    @Override
    public Set<Integer> getGameIdSet(String json) {
        return ((JSONArray) JsonPath.read(json, "$..apps[*].appid")).stream()
                .map(genre -> Integer.parseInt(genre.toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public GameDto getGameFullDto(String json) throws IOException {
        GameDto gameDto = new GameDto();
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        gameDto.setId(Integer.parseInt(valueOf(document, "$..data.steam_appid")));
        gameDto.setName(valueOf(document, "$..data.name"));
        gameDto.setDescription(valueOf(document, "$..data.about_the_game"));
        gameDto.setIcon(valueOf(document, "$..data.header_image"));
        gameDto.setDeveloper(valueOf(document, "$..data.developers[0]"));

        JSONArray genres = JsonPath.read(document, "$..data.genres[*].id");
        gameDto.setGenres(genres.stream()
                                .map(genre -> Integer.parseInt(genre.toString()))
                                .collect(Collectors.toSet()));
        return gameDto;
    }

    private String valueOf(Object document, String path) {
        return ((JSONArray)JsonPath.read(document, path)).get(0).toString();
    }
}
