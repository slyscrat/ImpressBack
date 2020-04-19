package com.slyscrat.impress.model.dto.game;

import com.slyscrat.impress.model.dto.DataTransferObject;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class GameDto extends DataTransferObject {
    @NotBlank
    private String name;
    @NotBlank
    private String icon;
    private String description;
    private String developer;
    private Set<String> screenshots = new HashSet<>();
    private Set<GameGenreDto> genres = new HashSet<>();
}
