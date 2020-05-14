package com.slyscrat.impress.model.dto.movie;

import com.slyscrat.impress.model.dto.DataTransferObject;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieDto extends DataTransferObject {
    @NotBlank
    private String name;
    @NotBlank
    private String poster;
    private String description;
    private Short duration;
    private Date releaseDate;
    private Set<MovieGenreDto> genres = new HashSet<>();
    private Set<MovieCrewDto> crew = new HashSet<>();
}
