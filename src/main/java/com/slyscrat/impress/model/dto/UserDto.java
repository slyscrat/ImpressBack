package com.slyscrat.impress.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends DataTransferObject {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickName;

    private String password;

/*    private Set<Integer> gameRates = new HashSet<>();
    private Set<Integer> movieRates = new HashSet<>();
    private Set<Integer> bookRates = new HashSet<>();*/
}
