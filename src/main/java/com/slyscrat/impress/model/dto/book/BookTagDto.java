package com.slyscrat.impress.model.dto.book;

import com.slyscrat.impress.model.dto.DataTransferObject;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "name")
@ToString
public class BookTagDto extends DataTransferObject {
    @NotBlank
    private String name;

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookTagDto bookTagDto = (BookTagDto) o;
        return name.equals(bookTagDto.getName());
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getName());
        return builder.toHashCode();
    }
    */
}
