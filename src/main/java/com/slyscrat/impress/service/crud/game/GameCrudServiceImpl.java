package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.game.GameDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.game.GameRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class GameCrudServiceImpl
        extends AbstractCrudService<GameEntity, GameDto, GameRepository>
        implements GameCrudService {

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
                .orElseThrow(() -> new EntityNotFoundException(GameEntity.class, dto.getId()));
        mapper.map(dto, gameEntity);
        return mapper.map(repository.save(gameEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
