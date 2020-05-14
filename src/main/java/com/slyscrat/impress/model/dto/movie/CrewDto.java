package com.slyscrat.impress.model.dto.movie;

import com.slyscrat.impress.model.dto.DataTransferObject;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class CrewDto extends DataTransferObject {
    @NotBlank
    private String name;
}
