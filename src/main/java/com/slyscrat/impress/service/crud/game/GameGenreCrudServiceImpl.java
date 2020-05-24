package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.game.GameGenreDto;
import com.slyscrat.impress.model.entity.GameGenreEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.game.GameGenreRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameGenreCrudServiceImpl
        extends AbstractCrudService<GameGenreEntity, GameGenreDto, GameGenreRepository>
        implements GameGenreCrudService {

    @Autowired
    public GameGenreCrudServiceImpl(GameGenreRepository repository,
                               Mapper<GameGenreEntity, GameGenreDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getIdsSet();
    }

    @Override
    public GameGenreDto create(GameGenreDto dto) {
        GameGenreEntity gameGenreEntity = new GameGenreEntity();
        mapper.map(dto, gameGenreEntity);
        return mapper.map(repository.save(gameGenreEntity));
    }

    @Override
    public GameGenreDto update(GameGenreDto dto) {
        GameGenreEntity gameGenreEntity = repository.findById(dto.getId()).orElse(new GameGenreEntity());
        mapper.map(dto, gameGenreEntity);
        return mapper.map(repository.save(gameGenreEntity));
    }

    @Override
    public List<GameGenreDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
