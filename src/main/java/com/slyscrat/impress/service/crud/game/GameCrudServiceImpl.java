package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.game.GameRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameCrudServiceImpl
        extends AbstractCrudService<GameEntity, GameDto, GameRepository>
        implements GameCrudService {

    @PersistenceContext
    EntityManager em;
    
    @Autowired
    public GameCrudServiceImpl(GameRepository repository,
                               Mapper<GameEntity, GameDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getIdsSet();
    }

    @Override
    public GameDto create(GameDto dto) {
        GameEntity gameEntity = new GameEntity();
        mapper.map(dto, gameEntity);
        return mapper.map(repository.save(gameEntity));
    }

    @Override
    public GameDto update(GameDto dto) {
        GameEntity gameEntity = repository.findById(dto.getId())
                .orElse(new GameEntity());
        mapper.map(dto, gameEntity);
        return mapper.map(repository.save(gameEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public List<GameDto> findAll(Pageable paging) {
        return repository.findAll(paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> findAllByGenre(Integer genreId, Pageable paging) {
        return repository.findAllByGenres_IdEquals(genreId, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> findAllByGenresIds(Set<Integer> ids, Pageable paging) {
        Set<Integer> gameIds = getGamesIdsByGenres(ids);
        return repository.findAllByIdIn(gameIds, paging).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    private Set<Integer> getGamesIdsByGenres(Set<Integer> genresIds) {
        StringBuilder sb = new StringBuilder("select\n" +
                "  games.id\n" +
                "from games\n" +
                "  left outer join genre_to_game ON games.id = genre_to_game.game_id\n" +
                "where\n" +
                "  true ");

        genresIds.forEach(genre -> {
            sb.append("and EXISTS (SELECT game_id FROM genre_to_game gm WHERE games.id = gm.game_id AND gm.genre_id = ");
            sb.append(genre.toString());
            sb.append(") ");
        });
        sb.append("group by games.id");

        return new HashSet<>(em.createNativeQuery(sb.toString()).getResultList());
    }
}
