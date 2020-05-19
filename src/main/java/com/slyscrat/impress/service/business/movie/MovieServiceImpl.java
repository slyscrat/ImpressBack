package com.slyscrat.impress.service.business.movie;

import com.slyscrat.impress.model.dto.movie.MovieDto;
import com.slyscrat.impress.service.business.AbstractBusinessService;
import com.slyscrat.impress.service.business.SortEnum;
import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import com.slyscrat.impress.service.crud.movie.MovieGenreCrudService;
import com.slyscrat.impress.service.recommendation.RecommendationSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl extends AbstractBusinessService implements MovieService{
    private final MovieCrudService movieCrudService;
    private final MovieGenreCrudService genreCrudService;
    private final RecommendationSystem recommendationSystem;

    @Override
    public List<MovieDto> getRecommendedList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        // return movieShortDto
        recommendationSystem.recommendMovie(userId);
        return null;
    }

    @Override
    public List<MovieDto> getFutureList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        // return MovieShortDto
        return null;
    }

    @Override
    public List<MovieDto> getRatedList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        // return MovieShortDto
        return null;
    }

    @Override
    public List<MovieDto> getAllList(Integer page, Integer sort, Set<Integer> genres) {
        // check Authorized -> return MovieDto or MovieShortDto
        Pageable paging = null;
        switch (checkSort(sort)) {
            case DateASC:
                paging = PageRequest.of(page, pageSize, Sort.by("releaseDate").ascending());
                break;
            case NameASC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").ascending());
                break;
            case NameDSC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").descending());
                break;
            default:
                paging = PageRequest.of(page, pageSize, Sort.by("releaseDate").descending());
                break;
        }
        if (genres.isEmpty()) return movieCrudService.findAll(paging);
        if (genres.size() == 1) return movieCrudService.findAllByGenre(genres.iterator().next(), paging);
        return movieCrudService.findAllByGenresIds(checkFilters(genres), paging);
    }

    @Override
    public List<MovieDto> getByNameList(Integer page, String name) {
        Pageable paging = PageRequest.of(page, pageSize);
        // check is Authorized
        // return MovieShortDto
        return null;
    }

    @Override
    public MovieDto getById(Integer id) {
        // check is Authorized
        // return MovieFullDto
        return null;
    }

    @Override
    protected Set<Integer> checkFilters(Set<Integer> inGenres) {
        if (inGenres == null || inGenres.isEmpty()) return inGenres;
        Set<Integer> finalGenres = genreCrudService.getIdsSet();
        return inGenres.stream()
                .filter(finalGenres::contains)
                .collect(Collectors.toSet());
    }

    @Override
    protected SortEnum checkSort(Integer sortMethod) {
        return SortEnum.findByKey(sortMethod);
    }
}
