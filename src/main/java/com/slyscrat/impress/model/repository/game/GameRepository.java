package com.slyscrat.impress.model.repository.game;

import com.slyscrat.impress.model.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {
    @Query("select g.id from GameEntity g order by g.id")
    Set<Integer> getAllAppIds();
}
