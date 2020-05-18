package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.ItemShortDto;
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
        MovieRateEntity movieRateEntity = repository.findById(dto.getId()).orElse(new MovieRateEntity());
        mapper.map(dto, movieRateEntity);
        return mapper.map(repository.save(movieRateEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }


    @Override
    public List<ItemShortDto> getShortInfoByUserId(Integer userId) {
        List<MovieRateEntity> movieRatesSet = new ArrayList<>(repository.findAllByUser_Id(userId));
        List<ItemShortDto> shortInfo = new ArrayList<>();
        movieRatesSet.forEach(rate -> shortInfo.add(new ItemShortDto(rate.getUser().getId(), rate.getMovie().getId())));
        return shortInfo;
    }

    @Override
    public ItemRateDto findByUserAndMovie(Integer userId, Integer movieId) {
        return mapper.map(repository.findByUser_IdAndMovie_Id(userId, movieId)
                .orElseThrow(() -> new EntityNotFoundException(MovieRateEntity.class, movieId)));
    }

    @Override
    public long count() {
        return repository.getCount();
    }
}
