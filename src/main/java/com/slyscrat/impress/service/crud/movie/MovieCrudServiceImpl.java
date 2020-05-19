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
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieCrudServiceImpl
        extends AbstractCrudService<MovieEntity, MovieDto, MovieRepository>
        implements MovieCrudService {

    @PersistenceContext
    EntityManager em;

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

    @Override
    public List<MovieDto> findAll(Pageable paging) {
        return repository.findAll(paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> findAllByGenre(Integer genreId, Pageable paging) {
        return repository.findAllByGenres_IdEquals(genreId, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> findAllByGenresIds(Set<Integer> ids, Pageable paging) {
        Set<Integer> movieIds = getMoviesIdsByGenres(ids);
        movieIds.forEach(System.out::println);
        return repository.findAllByIdIn(movieIds, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    private Set<Integer> getMoviesIdsByGenres(Set<Integer> genresIds) {
        StringBuilder sb = new StringBuilder("select\n" +
                "  movies.id\n" +
                "from movies\n" +
                "  left outer join genre_to_movie ON movies.id = genre_to_movie.movie_id\n" +
                "where\n" +
                "  true ");

        genresIds.forEach(genre -> {
            sb.append("and EXISTS (SELECT movie_id FROM genre_to_movie gm WHERE movies.id = gm.movie_id AND gm.genre_id = ");
            sb.append(genre.toString());
            sb.append(") ");
        });
        sb.append("group by movies.id");

        return new HashSet<>(em.createNativeQuery(sb.toString()).getResultList());
    }
}
