package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "game_rates")
@IdClass(GameRate.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameRateEntity extends AbstractDataBaseEntity{
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private UserEntity user;

    @Id
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
