package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.model.entity.MovieEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.movie.MovieRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class MovieCrudServiceImpl
        extends AbstractCrudService<MovieEntity, MovieDto, MovieRepository>
        implements MovieCrudService {

    @Autowired
    public MovieCrudServiceImpl(MovieRepository repository,
                               Mapper<MovieEntity, MovieDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public MovieDto create(MovieDto dto) {
        MovieEntity movieEntity = new MovieEntity();
        mapper.map(dto, movieEntity);
        return mapper.map(repository.save(movieEntity));
    }


    @Override
    public MovieDto update(MovieDto dto) {
        MovieEntity movieEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(MovieEntity.class, dto.getId()));
        mapper.map(dto, movieEntity);
        return mapper.map(repository.save(movieEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getAllIds();
    }
}
