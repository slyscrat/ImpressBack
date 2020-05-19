package com.slyscrat.impress.model.repository.movie;

import com.slyscrat.impress.model.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    @Query("select m.id from MovieEntity m order by m.id")
    Set<Integer> getAllIds();
    Page<MovieEntity> findAllByGenres_IdEquals(@NotNull Integer genres_id, Pageable pageable);
    Set<MovieEntity> findTop20ByNameContainsOrderByReleaseDateDesc(@NotNull String name);
    Page<MovieEntity> findAllByIdIn(Collection<@NotNull Integer> id, Pageable pageable);
}
