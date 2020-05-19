package com.slyscrat.impress.service.business.movie;

import com.slyscrat.impress.model.dto.movie.MovieDto;

import java.util.List;
import java.util.Set;

public interface MovieService {
    List<MovieDto> getRecommendedList(Integer page, Integer userId);
    List<MovieDto> getFutureList(Integer page, Integer userId);
    List<MovieDto> getRatedList(Integer page, Integer userId);
    List<MovieDto> getAllList(Integer page, Integer sort, Set<Integer> genres);
    List<MovieDto> getByNameList(Integer page, String name);
    MovieDto getById(Integer id);
}
