package com.slyscrat.impress.service.crud;

import com.slyscrat.impress.model.dto.UserDto;

public interface UserCrudService extends CrudService<UserDto> {
    boolean existByEmail(String email);
    UserDto findByEmail(String email);
    UserDto findByEmailPriv(String email);
    Integer getUserIdFromSecurityContext();
}
