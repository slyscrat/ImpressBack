package com.slyscrat.impress.service.crud.game;

import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.entity.GameRateEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.game.GameRateRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameRateCrudServiceImpl
        extends AbstractCrudService<GameRateEntity, ItemRateDto, GameRateRepository>
        implements GameRateCrudService {

    @Autowired
    public GameRateCrudServiceImpl(GameRateRepository repository,
                                   Mapper<GameRateEntity, ItemRateDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public ItemRateDto create(ItemRateDto dto) {
        GameRateEntity gameRateEntity = new GameRateEntity();
        mapper.map(dto, gameRateEntity);
        return mapper.map(repository.save(gameRateEntity));
    }

    @Override
    public ItemRateDto update(ItemRateDto dto) {
        GameRateEntity gameRateEntity = repository.findByUser_IdAndGame_Id(dto.getUser(), dto.getItem()).orElse(new GameRateEntity());
        mapper.map(dto, gameRateEntity);
        return mapper.map(repository.save(gameRateEntity));
    }

    @Override
    public void delete(Integer id) { }

    @Override
    public void delete(Integer userId, Integer gameId) {
        repository.findByUser_IdAndGame_Id(userId, gameId).ifPresent(repository::delete);
    }

    @Override
    public ItemRateDto findByUserAndGame(Integer userId, Integer gameId) {
        GameRateEntity rateEntity = repository.findByUser_IdAndGame_Id(userId, gameId).orElse(null);
        if (rateEntity != null) return mapper.map(rateEntity);
        return null;
    }

    @Override
    public long count() {
        return repository.getCount();
    }
}
