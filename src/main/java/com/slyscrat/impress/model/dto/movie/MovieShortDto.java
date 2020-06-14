package com.slyscrat.impress.model.dto.movie;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieShortDto extends MovieDto {
    private Short rate = -1;
    private boolean isFutured = false;

    public MovieShortDto(MovieDto movieDto) {
        super();
        this.setId(movieDto.getId());
        this.setName(movieDto.getName());
        this.setCrew(movieDto.getCrew());
        this.setGenres(movieDto.getGenres());
        this.setDescription(movieDto.getDescription());
        this.setDuration(movieDto.getDuration());
        this.setIcon(movieDto.getIcon());
        this.setReleaseDate(movieDto.getReleaseDate());
    }
}
