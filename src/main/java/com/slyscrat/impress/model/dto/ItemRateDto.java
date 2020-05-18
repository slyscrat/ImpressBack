package com.slyscrat.impress.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ItemRateDto extends DataTransferObject {
    @NotNull
    private Integer user;
    @NotNull
    private Integer item;
    @PositiveOrZero
    private Integer rate;
    private String note;
}
