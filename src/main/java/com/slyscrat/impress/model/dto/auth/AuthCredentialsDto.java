package com.slyscrat.impress.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthCredentialsDto implements Serializable {
    private String email;
    private String password;
}
