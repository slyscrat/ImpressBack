package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.model.entity.GameGenreEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
                .setPostConverter(convertToDto());
    }
}
