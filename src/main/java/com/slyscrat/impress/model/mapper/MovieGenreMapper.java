package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.model.entity.MovieGenreEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MovieGenreMapper extends AbstractMapper<MovieGenreEntity, MovieGenreDto> {
    @Autowired
    public MovieGenreMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> m.skip(MovieGenreEntity::setMovies))
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .setPostConverter(convertToDto());
    }
}
