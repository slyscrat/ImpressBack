package com.slyscrat.impress.model.dto.book;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class BookFullDto extends BookDto {
    private Short rate = -1;
    private boolean isFutured = false;
    private String note = "";

    public BookFullDto(BookDto bookDto) {
        super();
        this.setId(bookDto.getId());
        this.setName(bookDto.getName());
        this.setAuthor(bookDto.getAuthor());
        this.setTags(bookDto.getTags());
        this.setIcon(bookDto.getIcon());
        this.setPublicationDate(bookDto.getPublicationDate());
    }
}
