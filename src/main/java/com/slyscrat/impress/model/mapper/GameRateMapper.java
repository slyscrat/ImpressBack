package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.ItemRateDto;
import com.slyscrat.impress.model.entity.GameEntity;
import com.slyscrat.impress.model.entity.GameRateEntity;
import com.slyscrat.impress.model.entity.UserEntity;
import com.slyscrat.impress.model.repository.UserRepository;
import com.slyscrat.impress.model.repository.game.GameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GameRateMapper extends AbstractMapper<GameRateEntity, ItemRateDto> {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameRateMapper(GameRepository gameRepository,
                          ModelMapper modelMapper,
                          UserRepository userRepository) {
        super(modelMapper);
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(GameRateEntity::setId);
                    m.skip(GameRateEntity::setGame);
                    m.skip(GameRateEntity::setUser);
                })
                .setPostConverter(convertToEntity());
        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> {
                    m.skip(ItemRateDto::setItem);
                    m.skip(ItemRateDto::setUser);
                })
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(GameRateEntity source, ItemRateDto destination) {
        destination.setItem(source.getGame().getId());
        destination.setUser(source.getUser().getId());
    }

    @Override
    protected void mapSpecificFields(ItemRateDto source, GameRateEntity destination) {
        GameEntity gameEntity = gameRepository.findById(source.getItem())
                .orElseThrow(() -> new EntityNotFoundException(GameEntity.class, source.getItem()));
        UserEntity userEntity = userRepository.findById(source.getUser())
                .orElseThrow(() -> new EntityNotFoundException(GameEntity.class, source.getUser()));

        gameEntity.getRated().add(destination);
        userEntity.getGameRates().add(destination);

        destination.setGame(gameEntity);
        destination.setUser(userEntity);
    }
}

