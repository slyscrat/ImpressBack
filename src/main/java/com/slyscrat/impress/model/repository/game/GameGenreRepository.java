package com.slyscrat.impress.model.repository.game;

import com.slyscrat.impress.model.entity.GameGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GameGenreRepository extends JpaRepository<GameGenreEntity, Integer> {
    @Query("select g.id from GameGenreEntity g")
    Set<Integer> getIdsSet();

    @Query("select g from GameGenreEntity g order by g.name")
    List<GameGenreEntity> getAll();
}
