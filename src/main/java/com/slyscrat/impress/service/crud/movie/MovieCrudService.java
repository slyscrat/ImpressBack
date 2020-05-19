package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.service.crud.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface MovieCrudService extends CrudService<MovieDto> {
    Set<Integer> getIdsSet();
    List<MovieDto> findAll(Pageable paging);
    List<MovieDto> findAllByGenre(Integer genreId, Pageable paging);
    List<MovieDto> findAllByGenresIds(Set<Integer> ids, Pageable paging);
}
