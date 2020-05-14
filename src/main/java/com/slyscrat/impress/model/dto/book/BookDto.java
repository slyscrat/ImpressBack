package com.slyscrat.impress.model.dto.book;

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
public class BookDto extends DataTransferObject {
    @NotBlank
    private String name;
    @NotBlank
    private String icon;
    private String author;
    private String publicationDate;
    private Set<BookTagDto> tags = new HashSet<>();
}
