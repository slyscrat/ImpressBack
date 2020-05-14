package com.slyscrat.impress.model.repository.book;

import com.slyscrat.impress.model.entity.BookTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookTagRepository extends JpaRepository<BookTagEntity, Integer> {
    @Query("select bt from BookTagEntity bt")
    Set<BookTagEntity> getAllBookTags();

    BookTagEntity getBookTagEntityByName(String name);
}
