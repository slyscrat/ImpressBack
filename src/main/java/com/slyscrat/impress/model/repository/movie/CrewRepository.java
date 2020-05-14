package com.slyscrat.impress.model.repository.movie;

import com.slyscrat.impress.model.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CrewRepository extends JpaRepository<CrewEntity, Integer> {
    @Query("select c.id from CrewEntity c order by c.id")
    Set<Integer> getAllIds();
}
