package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.entity.MovieRateEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.movie.MovieRateRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class MovieRateCrudServiceImpl
        extends AbstractCrudService<MovieRateEntity, ItemRateDto, MovieRateRepository>
        implements MovieRateCrudService {

    @Autowired
    public MovieRateCrudServiceImpl(MovieRateRepository repository,
                                     Mapper<MovieRateEntity, ItemRateDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public ItemRateDto create(ItemRateDto dto) {
        MovieRateEntity movieRateEntity = new MovieRateEntity();
        mapper.map(dto, movieRateEntity);
        return mapper.map(repository.save(movieRateEntity));
    }

    @Override
    public ItemRateDto update(ItemRateDto dto) {
        MovieRateEntity movieRateEntity = repository.findByUser_IdAndMovie_Id(dto.getUser(), dto.getItem()).orElse(new MovieRateEntity());
        mapper.map(dto, movieRateEntity);
        return mapper.map(repository.save(movieRateEntity));
    }

    @Override
    public void delete(Integer id) { }

    @Override
    public void delete(Integer userId, Integer movieId) {
        repository.findByUser_IdAndMovie_Id(userId, movieId).ifPresent(repository::delete);
    }

    @Override
    public ItemRateDto findByUserAndMovie(Integer userId, Integer movieId) {
        MovieRateEntity rateEntity = repository.findByUser_IdAndMovie_Id(userId, movieId).orElse(null);
        if (rateEntity != null) return mapper.map(rateEntity);
        return null;
    }

    @Override
    public long count() {
        return repository.getCount();
    }
}
