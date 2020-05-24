package com.slyscrat.impress.model.repository.movie;

import com.slyscrat.impress.model.entity.MovieRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRateRepository extends JpaRepository<MovieRateEntity, Integer> {
    //List<MovieRateEntity> findAllByUser_Id(Integer user_id);

    Optional<MovieRateEntity> findByUser_IdAndMovie_Id(Integer user_id, Integer movie_id);

    @Query("SELECT count(mr) FROM MovieRateEntity mr")
    Integer getCount();

    Set<MovieRateEntity> findAllByMovie_Id(Integer movie_id);
}
