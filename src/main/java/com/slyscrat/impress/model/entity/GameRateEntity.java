package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "game_rates")
//@IdClass(GameRate.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(name="GAME_RATE_SEQ", sequenceName="gameRate_sequence")
public class GameRateEntity extends AbstractDataBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GAME_RATE_SEQ")
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
    @JoinColumn(name = "gameId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private GameEntity game;

    @Column(name = "note")
    private String note;

    @Column(name = "rate") //columnDefinition = "SMALLINT"
    private Short rate;
}
