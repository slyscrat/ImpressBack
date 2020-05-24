package com.slyscrat.impress.model.dto.game;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class GameFullDto extends GameDto {
    private Short rate = -1;
    private boolean isFutured = false;

    public GameFullDto(GameDto gameDto) {
        super();
        this.setId(gameDto.getId());
        this.setName(gameDto.getName());
        this.setDeveloper(gameDto.getDeveloper());
        this.setGenres(gameDto.getGenres());
        this.setDescription(gameDto.getDescription());
        this.setScreenshots(gameDto.getScreenshots());
        this.setIcon(gameDto.getIcon());
    }
}
