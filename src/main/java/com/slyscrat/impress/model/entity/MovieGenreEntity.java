package com.slyscrat.impress.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie_genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class MovieGenreEntity extends AbstractDataBaseEntity{
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(
            mappedBy = "genres",
            fetch = FetchType.LAZY
    )
    private Set<MovieEntity> movies = new HashSet<>();
}
