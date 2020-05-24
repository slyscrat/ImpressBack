package com.slyscrat.impress.model.repository.book;

import com.slyscrat.impress.model.entity.BookEntity;
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
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    @Query("select b.id from BookEntity b")
    Set<Integer> getIdsSet();
    Page<BookEntity> findAllByRated_User_Id_AndRated_Rate(@NotNull Integer user_id, @NotNull Short rate, Pageable pageable);

    Page<BookEntity> findAllByRated_User_Id_AndRated_RateGreaterThan(@NotNull Integer user_id, @NotNull Short rate, Pageable pageable);
    Page<BookEntity> findAllByTags_IdEquals(@NotNull Integer tags_id, Pageable pageable);

    Page<BookEntity> findAllByNameContainsIgnoreCase(@NotNull String name, Pageable pageable);

    Page<BookEntity> findAllByIdIn(Collection<@NotNull Integer> ids, Pageable pageable);
    List<BookEntity> findAllByIdIn(Collection<@NotNull Integer> ids);
}
