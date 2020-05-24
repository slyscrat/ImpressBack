package com.slyscrat.impress.service.business.movie;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.model.dto.movie.MovieGenreDto;

import java.util.List;
import java.util.Set;

public interface MovieService {

    List<MovieDto> getRecommendedList(Integer page, Integer userId);
    List<MovieDto> getFutureList(Integer page, Integer userId);
    List<MovieDto> getRatedList(Integer page, Integer userId);
    List<MovieDto> getAllList(Integer page, Integer sort, Set<Integer> genres, Integer userId);
    List<MovieDto> getByNameList(Integer page, String name, Integer userId);

    MovieDto getById(Integer id, Integer userId);
    ItemRateDto rate(Integer movieId, Short rate, Integer userId);
    ItemRateDto note(Integer movieId, String note, Integer userId);

    List<MovieGenreDto> getGenres();

    void noteDel(Integer movieId, Integer userId);
}
