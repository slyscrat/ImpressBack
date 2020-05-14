package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.model.entity.MovieGenreEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.movie.MovieGenreRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class MovieGenreCrudServiceImpl
        extends AbstractCrudService<MovieGenreEntity, MovieGenreDto, MovieGenreRepository>
        implements MovieGenreCrudService {

    @Autowired
    public MovieGenreCrudServiceImpl(MovieGenreRepository repository,
                               Mapper<MovieGenreEntity, MovieGenreDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getIdsSet();
    }

    @Override
    public MovieGenreDto create(MovieGenreDto dto) {
        MovieGenreEntity movieGenreEntity = new MovieGenreEntity();
        mapper.map(dto, movieGenreEntity);
        return mapper.map(repository.save(movieGenreEntity));
    }

    @Override
    public MovieGenreDto update(MovieGenreDto dto) {
        MovieGenreEntity movieGenreEntity = repository.findById(dto.getId()).orElse(new MovieGenreEntity());
        mapper.map(dto, movieGenreEntity);
        return mapper.map(repository.save(movieGenreEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
