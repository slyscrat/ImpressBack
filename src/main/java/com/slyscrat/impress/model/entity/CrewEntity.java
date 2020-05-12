package com.slyscrat.impress.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "crew")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrewEntity extends AbstractDataBaseEntity{
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "crew",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<MovieCrewEntity> movies = new HashSet<>();
}
