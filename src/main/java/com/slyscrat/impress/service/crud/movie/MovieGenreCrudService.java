package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.movie.MovieGenreDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.Set;

public interface MovieGenreCrudService extends CrudService<MovieGenreDto> {
    Set<Integer> getIdsSet();
}
