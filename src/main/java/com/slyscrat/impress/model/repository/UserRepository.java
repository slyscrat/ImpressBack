package com.slyscrat.impress.model.repository;

import com.slyscrat.impress.model.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    @Query("select u.id from UserEntity u where u.email = ?1")
    Optional<Integer> findIdByEmail(String email);
    @Query("select u from UserEntity u where u.id != 1")
    @NotNull
    Page<UserEntity> findAll(Pageable page);
}
