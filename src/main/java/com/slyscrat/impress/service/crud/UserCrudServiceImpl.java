package com.slyscrat.impress.service.crud;

import com.slyscrat.impress.exception.EntityAlreadyExistsException;
import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.model.dto.UserDto;
import com.slyscrat.impress.model.entity.UserEntity;
import com.slyscrat.impress.model.mapper.Mapper;
import com.slyscrat.impress.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserCrudServiceImpl
        extends AbstractCrudService<UserEntity, UserDto, UserRepository>
        implements UserCrudService {
    @Autowired
    public UserCrudServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDto create(UserDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EntityAlreadyExistsException(UserEntity.class, dto.getEmail());
        }

        UserEntity userEntity = new UserEntity();
        mapper.map(dto, userEntity);
        UserDto dtoRes = mapper.map(repository.save(userEntity));
        dtoRes.setPassword(null);
        return dtoRes;
    }

    @Override
    public UserDto update(UserDto dto) {
        UserEntity userEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, dto.getId()));

        mapper.map(dto, userEntity);
        UserDto dtoRes = mapper.map(repository.save(userEntity));
        dtoRes.setPassword(null);
        return dtoRes;
    }

    @Override
    public UserDto findByEmail(String email) {
        return mapper.map(repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, email)));
    }

    @Override
    public UserDto findByEmailPriv(String email) {
        UserDto dtoRes = findByEmail(email);
        dtoRes.setPassword(null);
        return dtoRes;
    }

    @Override
    public void delete(Integer id) {
        if (id != 1)
            repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public UserDto findById(Integer id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, id));

        UserDto dto = mapper.map(userEntity);
        dto.setPassword(null);
        return dto;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public Set<UserDto> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).stream().filter(item -> item.getId() != 1)
                .map(item -> {
                    UserDto dtoRes = mapper.map(item);
                    dtoRes.setPassword(null);
                    return dtoRes;
                })
                .collect(Collectors.toSet());
    }

    public Integer getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return repository.findIdByEmail(authentication.getName()).orElse(0);
    }
}
