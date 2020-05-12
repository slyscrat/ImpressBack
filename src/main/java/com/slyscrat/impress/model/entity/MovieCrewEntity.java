package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "crew_to_movie")
@IdClass(MovieCrew.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieCrewEntity extends AbstractDataBaseEntity{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private CrewEntity crew;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private MovieEntity movie;

    @Column(name = "post")
    private String post;
}
