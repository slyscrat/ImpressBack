package com.slyscrat.impress.model.entity;

import lombok.*;

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

    @Id
    @Column(name = "post")
    private String post;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MovieCrewEntity that = (MovieCrewEntity) o;

        if (getCrew().getId() != null && getMovie().getId() != null ?
                !getCrew().getId().equals(that.getCrew().getId()) || !getMovie().getId().equals(that.getMovie().getId())
                : that.getCrew().getId() != null && that.getMovie().getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getCrew().getId().hashCode()*getMovie().getId().hashCode();
    }
}
