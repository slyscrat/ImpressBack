package com.slyscrat.impress.service.crud.movie;

import com.slyscrat.impress.model.dto.movie.CrewDto;
import com.slyscrat.impress.model.entity.CrewEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.movie.CrewRepository;
import com.slyscrat.impress.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CrewCrudServiceImpl
        extends AbstractCrudService<CrewEntity, CrewDto, CrewRepository>
        implements CrewCrudService {

    @Autowired
    public CrewCrudServiceImpl(CrewRepository repository,
                                     Mapper<CrewEntity, CrewDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public CrewDto create(CrewDto dto) {
        CrewEntity crewEntity = new CrewEntity();
        mapper.map(dto, crewEntity);
        return mapper.map(repository.save(crewEntity));
    }

    @Override
    public CrewDto update(CrewDto dto) {
        CrewEntity crewEntity = repository.findById(dto.getId()).orElse(new CrewEntity());
        mapper.map(dto, crewEntity);
        return mapper.map(repository.save(crewEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<Integer> getIdsSet() {
        return repository.getAllIds();
    }
}
