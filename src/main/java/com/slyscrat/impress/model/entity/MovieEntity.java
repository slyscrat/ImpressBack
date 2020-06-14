package com.slyscrat.impress.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieEntity extends AbstractDataBaseEntity {
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Short duration;

    @Column(name = "releaseDate", nullable = false)
    private Date releaseDate;

    @NotNull
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "genre_to_movie",
            joinColumns = {@JoinColumn(name = "movieId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genreId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<MovieGenreEntity> genres = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "movie",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<MovieCrewEntity> crew = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "movie",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<MovieRateEntity> rated = new HashSet<>();
}
