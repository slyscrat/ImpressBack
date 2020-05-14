package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.movie.CrewDto;
import com.slyscrat.impress.model.dto.movie.MovieCrewDto;
import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.model.entity.CrewEntity;
import com.slyscrat.impress.model.entity.MovieCrewEntity;
import com.slyscrat.impress.model.entity.MovieEntity;
import com.slyscrat.impress.model.entity.MovieGenreEntity;
import com.slyscrat.impress.model.repository.movie.CrewRepository;
import com.slyscrat.impress.model.repository.movie.MovieGenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MovieMapper extends AbstractMapper<MovieEntity, MovieDto> {

    private final MovieGenreRepository movieGenreRepository;
    private final CrewRepository crewRepository;
    StringBuilder stringBuilder;

    @Autowired
    public MovieMapper(MovieGenreRepository movieGenreRepository,
                       ModelMapper modelMapper,
                       CrewRepository crewRepository) {
        super(modelMapper);
        this.movieGenreRepository = movieGenreRepository;
        this.crewRepository = crewRepository;
        stringBuilder = new StringBuilder();
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(MovieEntity::setCrew);
                    m.skip(MovieEntity::setGenres);
                    m.skip(MovieEntity::setRatedBy);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> {
                    m.skip(MovieDto::setCrew);
                    m.skip(MovieDto::setGenres);
                })
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(MovieEntity source, MovieDto destination) {
        destination.setGenres(source.getGenres()
                .stream()
                .map(genre -> {
                    MovieGenreDto dto = new MovieGenreDto();
                    dto.setId(genre.getId());
                    dto.setName(genre.getName());
                    return dto;
                })
                .collect(Collectors.toSet()));
        destination.setCrew(source.getCrew()
                .stream()
                .map(crew -> {
                    MovieCrewDto dto = new MovieCrewDto();
                    dto.setCrewId(crew.getCrew().getId());
                    dto.setMovieId(crew.getMovie().getId());
                    dto.setPost(crew.getPost());
                    return dto;
                })
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(MovieDto source, MovieEntity destination) {
        destination.getGenres().forEach(movieGenreEntity -> movieGenreEntity.getMovies().remove(destination));
        destination.getGenres().clear();
        source.getGenres().forEach(genre -> {
            MovieGenreEntity genreEntity = movieGenreRepository.findById(genre.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MovieGenreEntity.class, genre));
            genreEntity.getMovies().add(destination);
            destination.getGenres().add(genreEntity);
        });

        Set<MovieCrewEntity> set = new HashSet<>(source.getCrew().size());
        for (MovieCrewDto crew : source.getCrew()) {
            CrewEntity crewEntity = crewRepository.findById(crew.getCrewId())
                    .orElseThrow(() -> new EntityNotFoundException(CrewEntity.class, crew.getCrewId()));
            MovieCrewEntity movieCrewEntity = new MovieCrewEntity();
            movieCrewEntity.setCrew(crewEntity);
            movieCrewEntity.setPost(crew.getPost());
            movieCrewEntity.setMovie(destination);
            set.add(movieCrewEntity);
        }
        destination.setCrew(set);

    }
}

