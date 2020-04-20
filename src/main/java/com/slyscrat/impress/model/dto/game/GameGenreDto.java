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
public class GameGenreDto extends DataTransferObject {
    @NotBlank
    private String description;

    //private Set<Integer> games = new HashSet<>();
}
