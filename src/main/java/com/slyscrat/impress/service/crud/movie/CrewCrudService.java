package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.movie.CrewDto;
import com.slyscrat.impress.service.crud.CrudService;

import java.util.Set;

public interface CrewCrudService extends CrudService<CrewDto> {
    Set<Integer> getIdsSet();
}
