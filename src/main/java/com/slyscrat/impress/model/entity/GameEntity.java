    package com.slyscrat.impress.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//@Entity
//@Table(name = "games")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "day", nullable = false)
    private String icon; //"https://steamcdn-a.akamaihd.net/steam/apps/" + appId + "/header.jpg";

    @NotNull
    @Column(name = "developer", nullable = false)
    private String developer;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    /*
    @ManyToMany(
        mappedBy = "games",
        fetch = FetchType.LAZY
    )
    private Set<GameGenreEntity> genres = new HashSet<>();
    */


    // For DEL
    @Override
    public String toString(){
        return getId() + " : " + name;
    }
}
