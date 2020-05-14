package com.slyscrat.impress.model.repository.book;

import com.slyscrat.impress.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    @Query("select b.id from BookEntity b")
    Set<Integer> getIdsSet();
}
