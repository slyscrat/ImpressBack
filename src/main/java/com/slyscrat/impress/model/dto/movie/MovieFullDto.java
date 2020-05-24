package com.slyscrat.impress.model.dto.movie;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieFullDto extends MovieShortDto {
    private String note = "";
    private Set<CrewDto> team = new HashSet<>();
    private Set<CrewDto> actors = new HashSet<>();

    public MovieFullDto(MovieDto movieDto) {
        super(movieDto);
    }
}
