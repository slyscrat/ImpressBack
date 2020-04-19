package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "game_genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameGenreEntity extends AbstractDataBaseEntity{
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(
            mappedBy = "genres",
            fetch = FetchType.LAZY
    )
    private Set<GameEntity> games = new HashSet<>();
}