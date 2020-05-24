package com.slyscrat.impress.service.business.movie;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.dto.movie.*;
import com.slyscrat.impress.model.entity.MovieEntity;
import com.slyscrat.impress.model.mapper.MovieMapper;
import com.slyscrat.impress.model.repository.movie.MovieRepository;
import com.slyscrat.impress.service.business.AbstractBusinessService;
import com.slyscrat.impress.service.business.SortEnum;
import com.slyscrat.impress.service.crud.movie.CrewCrudService;
import com.slyscrat.impress.service.crud.movie.MovieCrudService;
import com.slyscrat.impress.service.crud.movie.MovieGenreCrudService;
import com.slyscrat.impress.service.crud.movie.MovieRateCrudService;
import com.slyscrat.impress.service.recommendation.RecommendationSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl extends AbstractBusinessService implements MovieService{
    private final MovieCrudService movieCrudService;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieGenreCrudService genreCrudService;
    private final CrewCrudService crewCrudService;
    private final MovieRateCrudService movieRateCrudService;
    private final RecommendationSystem recommendationSystem;

    private static final String RELEASE_DATE = "releaseDate";

    @Override
    public void noteDel(Integer movieId, Integer userId) {
        if (!isMoviePresent(movieId)) return;
        ItemRateDto itemRateDto = movieRateCrudService.findByUserAndMovie(userId, movieId);
        if (itemRateDto != null) {
            itemRateDto.setNote("");
            movieRateCrudService.update(itemRateDto);
        }
    }

    @Override
    public ItemRateDto rate(Integer movieId, Short rate, Integer userId) {
        if (!isMoviePresent(movieId)) return null;
        ItemRateDto itemRateDto = movieRateCrudService.findByUserAndMovie(userId, movieId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(movieId);
            itemRateDto.setUser(userId);
            itemRateDto.setRate(rate);
        }
        else if (itemRateDto.getRate().equals(rate)) {
            if (itemRateDto.getNote().length() == 0) {
                movieRateCrudService.delete(userId, movieId);
                itemRateDto.setUser(0);
                return itemRateDto;
            }
            itemRateDto.setRate((short) 0);
        }
        else itemRateDto.setRate(rate);
        return movieRateCrudService.update(itemRateDto);
    }

    @Override
    public ItemRateDto note(Integer movieId, String note, Integer userId) {
        if (!isMoviePresent(movieId)) return null;
        ItemRateDto itemRateDto = movieRateCrudService.findByUserAndMovie(userId, movieId);
        if (itemRateDto == null) {
            itemRateDto = new ItemRateDto();
            itemRateDto.setItem(movieId);
            itemRateDto.setUser(userId);
        }
        itemRateDto.setNote(note);
        return movieRateCrudService.update(itemRateDto);
    }

    @Override
    public List<MovieGenreDto> getGenres() {
        return genreCrudService.getAll();
    }

    private boolean isMoviePresent(Integer movieId) {
        return movieRepository.findById(movieId).isPresent();
    }

    @Override
    public List<MovieDto> getRecommendedList(Integer page, Integer userId) {
        List<Integer> ids = recommendationSystem.recommendMovie(userId);
        int startIndex = page < 0 ? 0 : page * pageSize;
        int endIndex = page < 1 ? pageSize : (page + 1) * pageSize;
        ids = ids.subList(startIndex, endIndex);
        List<MovieEntity> movies = movieRepository.findAllByIdIn(ids);
        List<Integer> finalIds = ids;
        movies.sort(Comparator.comparing(movie -> finalIds.indexOf(movie.getId())));
        return movies
                .stream()
                .map(movieMapper::map)
                .map(this::mapToShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getFutureList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<MovieDto> res = new ArrayList<>();
        movieRepository.findAllByRated_User_Id_AndRated_Rate(userId, (short) 0, paging)
                .forEach(movieEntity -> res.add(mapToShort((short) 0, movieMapper.map(movieEntity))));
        return res;
    }

    @Override
    public List<MovieDto> getRatedList(Integer page, Integer userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        List<MovieDto> res = new ArrayList<>();
        movieRepository.findAllByRated_User_Id_AndRated_RateGreaterThan(userId, (short) 0, paging)
                .forEach(movieEntity -> res.add(mapToShort(movieMapper.map(movieEntity), userId)));
        return res;
    }

    @Override
    public List<MovieDto> getAllList(Integer page, Integer sort, Set<Integer> genres, Integer userId) {
        Pageable paging = null;
        switch (checkSort(sort)) {
            case DateASC:
                paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).ascending());
                break;
            case NameASC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").ascending());
                break;
            case NameDSC:
                paging = PageRequest.of(page, pageSize, Sort.by("name").descending());
                break;
            default:
                paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).descending());
                break;
        }

        if (genres == null || genres.isEmpty())
            return movieCrudService.findAll(paging).stream()
                    .map(item -> mapToShort(item, userId))
                    .collect(Collectors.toList());

        if (genres.size() == 1)
            return movieCrudService.findAllByGenre(genres.iterator().next(), paging).stream()
                    .map(item -> mapToShort(item, userId))
                    .collect(Collectors.toList());

        return movieCrudService.findAllByGenresIds(checkFilters(genres), paging).stream()
                .map(item -> mapToShort(item, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getByNameList(Integer page, String name, Integer userId) {
        if (name == null) return getAllList(page, 0, null, userId);
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(RELEASE_DATE).descending());
        return movieRepository.findAllByNameContainsIgnoreCase(name, paging).toList().stream()
                .map(movieMapper::map)
                .map(movieDto -> mapToShort(movieDto, userId))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getById(Integer id, Integer userId) {
        return mapToFull(movieCrudService.findById(id), userId);
    }

    private MovieFullDto mapToFull(MovieDto movieDto, Integer userId) {
        MovieFullDto movieFullDto = new MovieFullDto(movieDto);
        ItemRateDto rate = null;
        if (userId != 0)
            rate = movieRateCrudService.findByUserAndMovie(userId,movieDto.getId());
        if (rate != null) {
            movieFullDto.setRate(rate.getRate());
            movieFullDto.setFutured(rate.getRate() == 0);
            movieFullDto.setNote(rate.getNote());
        }
        movieFullDto.getCrew().forEach(crewItem -> {
            CrewDto men = crewCrudService.findById(crewItem.getCrewId());
            if (crewItem.getPost().equals("Crew")) movieFullDto.getActors().add(men);
            else movieFullDto.getTeam().add(men);
        });
        return movieFullDto;
    }

    private MovieShortDto mapToShort(MovieDto movieDto, Integer userId) {
        MovieShortDto movieShortDto = new MovieShortDto(movieDto);
        ItemRateDto rate = null;
        if (userId != 0)
            rate = movieRateCrudService.findByUserAndMovie(userId,movieDto.getId());
        if (rate != null) {
            movieShortDto.setRate(rate.getRate());
            movieShortDto.setFutured(rate.getRate() == 0);
        }
        return movieShortDto;
    }

    private MovieShortDto mapToShort(MovieDto movieDto) {
        MovieShortDto movieShortDto = new MovieShortDto(movieDto);
        movieShortDto.setRate((short) -1);
        movieShortDto.setFutured(false);
        return movieShortDto;
    }

    private MovieShortDto mapToShort(Short rate, MovieDto movieDto) {
        MovieShortDto movieShortDto = mapToShort(movieDto);
        movieShortDto.setRate(rate);
        movieShortDto.setFutured(true);
        return movieShortDto;
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
