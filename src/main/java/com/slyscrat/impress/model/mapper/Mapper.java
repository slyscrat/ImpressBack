package com.slyscrat.impress.model.mapper;

import com.slyscrat.impress.model.dto.DataTransferObject;
import com.slyscrat.impress.model.entity.AbstractDataBaseEntity;

public interface Mapper<E extends AbstractDataBaseEntity, D extends DataTransferObject> {
    D map(E source);

    void map(D source, E destination);
}
