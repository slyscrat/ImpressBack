package com.slyscrat.impress.service.scheduled.games;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slyscrat.impress.model.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SteamJsonParserServiceImpl implements SteamJsonParserService{
    private final ObjectMapper objectMapper;

    @Override
    public Set<Long> getGameIdSet() {
        // if (i < 3 || isContainsHanScript) skip;
        return null;
    }

    @Override
    public GameDto getGameFullDto() {
        return null;
    }

    private boolean isContainsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }
}
