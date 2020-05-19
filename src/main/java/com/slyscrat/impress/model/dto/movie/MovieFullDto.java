package com.slyscrat.impress.model.dto.movie;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieFullDto extends MovieShortDto {
    private String note;
}
