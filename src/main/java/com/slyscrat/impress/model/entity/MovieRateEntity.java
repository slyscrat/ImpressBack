package com.slyscrat.impress.model.entity;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "movie_rates")
//@IdClass(MovieRate.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(name="MOVIE_RATE_SEQ", sequenceName="movieRate_sequence")
public class MovieRateEntity extends AbstractDataBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_RATE_SEQ")
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    //@Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private UserEntity user;

    //@Id
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

   /* @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieRateEntity movieRateEntity = (MovieRateEntity) o;
        return getUser().getId().equals(movieRateEntity.getUser().getId()) && getMovie().getId().equals(movieRateEntity.getMovie().getId());
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getUser());
        builder.append(getMovie());
        return builder.toHashCode();
    }*/
}
