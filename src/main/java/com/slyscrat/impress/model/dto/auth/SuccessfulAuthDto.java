package com.slyscrat.impress.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulAuthDto implements Serializable {
	private Integer userId;
	private String token;
}
