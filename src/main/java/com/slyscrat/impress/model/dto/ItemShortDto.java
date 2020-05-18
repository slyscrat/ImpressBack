package com.slyscrat.impress.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.grouplens.lenskit.data.event.Event;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemShortDto implements Event {
    private Integer user;
    private Integer item;

    @Override
    public long getUserId() {
        return user;
    }

    @Override
    public long getItemId() {
        return item;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }
}
