package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.entity.GameGenreEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class GameGenreMapper extends AbstractMapper<GameGenreEntity, GameGenreDto> {
    @Autowired
    public GameGenreMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> m.skip(GameGenreEntity::setGames))
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> m.skip(GameGenreDto::setGames))
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(GameGenreEntity source, GameGenreDto destination) {
        destination.setGames(source.getGames()
                .stream()
                .map(GameEntity::getId)
                .collect(Collectors.toSet()));
    }
}
