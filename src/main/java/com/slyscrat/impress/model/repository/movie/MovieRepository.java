package com.slyscrat.impress.model.repository.movie;

import com.slyscrat.impress.model.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    @Query("select m.id from MovieEntity m order by m.id")
    Set<Integer> getAllIds();
}
