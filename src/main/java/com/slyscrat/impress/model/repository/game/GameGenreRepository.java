package com.slyscrat.impress.model.repository.game;

import com.slyscrat.impress.model.entity.GameGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameGenreRepository extends JpaRepository<GameGenreEntity, Integer> {
    @Query("select gg.id from GameGenreEntity gg")
    Set<Integer> getAllIds();
}
