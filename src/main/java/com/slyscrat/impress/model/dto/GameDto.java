package com.slyscrat.impress.model.dto;

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
    private Set<Integer> genres = new HashSet<>();
}
