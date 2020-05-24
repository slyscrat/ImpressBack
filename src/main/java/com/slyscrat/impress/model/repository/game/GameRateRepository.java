package com.slyscrat.impress.model.repository.game;

import com.slyscrat.impress.model.entity.GameRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GameRateRepository extends JpaRepository<GameRateEntity, Integer> {
    Optional<GameRateEntity> findByUser_IdAndGame_Id(Integer user_id, Integer game_id);

    @Query("SELECT count(mr) FROM GameRateEntity mr")
    Integer getCount();

    Set<GameRateEntity> findAllByGame_Id(Integer game_id);
}
