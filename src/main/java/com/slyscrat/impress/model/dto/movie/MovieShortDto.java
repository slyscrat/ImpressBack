package com.slyscrat.impress.model.dto.movie;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieShortDto extends MovieDto{
    private Integer rate = 0;
    private boolean isFutured;
}
