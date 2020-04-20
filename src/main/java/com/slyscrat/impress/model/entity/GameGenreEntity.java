package com.slyscrat.impress.model.entity;

import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
public class GameGenreEntity extends AbstractDataBaseEntity{
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @EqualsAndHashCode.Exclude
    @ManyToMany(
            mappedBy = "genres",
            fetch = FetchType.LAZY
    )
    private Set<GameEntity> games = new HashSet<>();

    @Override
    public String toString() {
        return id + " : " + description;
    }
}