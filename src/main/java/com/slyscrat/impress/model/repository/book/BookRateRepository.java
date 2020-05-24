package com.slyscrat.impress.model.repository.book;

import com.slyscrat.impress.model.entity.BookRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRateRepository extends JpaRepository<BookRateEntity, Integer> {

    Optional<BookRateEntity> findByUser_IdAndBook_Id(Integer user_id, Integer book_id);

    @Query("SELECT count(mr) FROM BookRateEntity mr")
    Integer getCount();

    Set<BookRateEntity> findAllByBook_Id(Integer book_id);
}
