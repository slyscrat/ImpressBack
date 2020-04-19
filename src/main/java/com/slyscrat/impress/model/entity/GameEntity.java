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
@Table(name = "games")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)
public class GameEntity extends AbstractDataBaseEntity {
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "day", nullable = false)
    private String icon;
    //"https://steamcdn-a.akamaihd.net/steam/apps/" + appId + "/header.jpg";

    @Column(name = "developer", nullable = false)
    private String developer;

    @NotNull
    @Column(name = "description", nullable = false, length = 800)
    private String description;

    @NotNull
    @Column(name = "screenshots", nullable = false, length = 800)
    private String screenshots;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "genre_to_game",
            joinColumns = {@JoinColumn(name = "gameId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genreId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<GameGenreEntity> genres = new HashSet<>();

    // For DEL
    @Override
    public String toString(){
        return getId() + " : " + name;
    }
}
