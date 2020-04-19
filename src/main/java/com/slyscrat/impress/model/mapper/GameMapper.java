package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.entity.GameGenreEntity;
import com.slyscrat.impress.model.repository.game.GameGenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameMapper extends AbstractMapper<GameEntity, GameDto> {

    private final GameGenreRepository gameGenreRepository;
    StringBuilder stringBuilder;

    @Autowired
    public GameMapper(GameGenreRepository gameGenreRepository,
                      ModelMapper modelMapper) {
        super(modelMapper);
        this.gameGenreRepository = gameGenreRepository;
        stringBuilder = new StringBuilder();
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(GameEntity::setScreenshots);
                    m.skip(GameEntity::setGenres);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> {
                    m.skip(GameDto::setScreenshots);
                    m.skip(GameDto::setGenres);
                })
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(GameEntity source, GameDto destination) {
        destination.setScreenshots(Arrays.stream(source.getScreenshots().split(";")).collect(Collectors.toSet()));
        destination.setGenres(source.getGenres()
                .stream()
                .map(genre -> {
                    GameGenreDto dto = new GameGenreDto();
                    dto.setId(genre.getId());
                    dto.setDescription(genre.getDescription());
                    return dto;
                })
                .collect(Collectors.toSet()));
    }

    // TODO : remove System.out
    @Override
    protected void mapSpecificFields(GameDto source, GameEntity destination) {
        source.getScreenshots().forEach(screen -> stringBuilder.append(screen).append(";"));
        System.out.println("scr= " + stringBuilder.toString().length());
        System.out.println("check= " + stringBuilder.toString());
        destination.setScreenshots(stringBuilder.toString());
        stringBuilder.setLength(0);
        System.out.println("empty= " + stringBuilder.toString());

        source.getGenres().forEach(genre -> {
            System.out.println("gen= " + genre);
            GameGenreEntity genreEntity = gameGenreRepository.findById(genre.getId())
                    .orElseThrow(() -> new EntityNotFoundException(GameGenreEntity.class, genre));
            genreEntity.getGames().add(destination);
            destination.getGenres().add(genreEntity);
        });
        System.out.println("ent= " +destination);
    }
}

