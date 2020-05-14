package com.slyscrat.impress.model.dto.movie;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class CrewFullDto extends CrewDto{
    private String post;
}
