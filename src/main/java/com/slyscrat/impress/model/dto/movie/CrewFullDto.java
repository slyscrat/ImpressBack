package com.slyscrat.impress.model.dto.movie;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class CrewFullDto extends CrewDto{
    private String post;

    public CrewFullDto(CrewDto crewDto) {
        super();
        this.setId(crewDto.getId());
        this.setName(crewDto.getName());
    }
}
