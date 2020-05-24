package com.slyscrat.impress.model.repository.game;

import com.slyscrat.impress.model.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {
    @Query("select g.id from GameEntity g order by g.id")
    Set<Integer> getIdsSet();
    Page<GameEntity> findAllByRated_User_Id_AndRated_Rate(@NotNull Integer user_id, @NotNull Short rate, Pageable pageable);

    Page<GameEntity> findAllByRated_User_Id_AndRated_RateGreaterThan(@NotNull Integer user_id, @NotNull Short rate, Pageable pageable);
    Page<GameEntity> findAllByGenres_IdEquals(@NotNull Integer genres_id, Pageable pageable);

    Page<GameEntity> findAllByNameContainsIgnoreCase(@NotNull String name, Pageable pageable);

    Page<GameEntity> findAllByIdIn(Collection<@NotNull Integer> ids, Pageable pageable);
    List<GameEntity> findAllByIdIn(Collection<@NotNull Integer> ids);
}
