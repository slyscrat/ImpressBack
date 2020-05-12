package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movie_rates")
@IdClass(MovieRate.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieRateEntity extends AbstractDataBaseEntity{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private MovieEntity movie;

    @Column(name = "note")
    private String note;

    @Column(name = "rate") //columnDefinition = "SMALLINT"
    private Short rate;
}
