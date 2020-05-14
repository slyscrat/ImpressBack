package com.slyscrat.impress.model.dto.movie;

import com.slyscrat.impress.model.dto.DataTransferObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MovieCrewDto extends DataTransferObject {
    private Integer crewId;
    private Integer movieId;
    private String post;
}
