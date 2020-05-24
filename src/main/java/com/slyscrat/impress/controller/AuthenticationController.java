package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.UserDto;
import com.slyscrat.impress.model.dto.auth.AuthCredentialsDto;
import com.slyscrat.impress.model.dto.auth.SuccessfulAuthDto;
import com.slyscrat.impress.service.crud.UserCrudService;
import com.slyscrat.impress.service.security.UserAuthenticationService;
import com.slyscrat.impress.service.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final UserAuthenticationService authenticationService;
	private final UserCrudService userCrudService;

	@PostMapping(value = "/login")
	public ResponseEntity<SuccessfulAuthDto> login(@RequestBody AuthCredentialsDto credentials) {
		return ResponseEntity.ok(authenticationService.login(credentials));
	}

	@PostMapping(value = "/registration")
	public ResponseEntity<String> register(@RequestBody UserDto userDto) {
		if (userCrudService.create(userDto) != null)
			return ResponseEntity.ok("done");
		else return ResponseEntity.badRequest().body("email already taken");
	}

	@PostMapping(value = "/logout")
	public ResponseEntity<String> logout(@RequestBody UserDto userDto, HttpServletRequest request) {
		authenticationService.logout(request);
		return ResponseEntity.ok("done");
	}
}
