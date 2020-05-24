package com.slyscrat.impress.service.security;

import com.slyscrat.impress.model.dto.UserDto;
import com.slyscrat.impress.model.dto.auth.AuthCredentialsDto;
import com.slyscrat.impress.model.dto.auth.SuccessfulAuthDto;
import com.slyscrat.impress.model.dto.auth.UserRole;
import com.slyscrat.impress.service.crud.UserCrudService;
import com.slyscrat.impress.service.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
	private final UserCrudService userCrudService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public SuccessfulAuthDto login(AuthCredentialsDto credentials) {
		if (!userCrudService.existByEmail(credentials.getEmail())) {
			throw new BadCredentialsException("Incorrect credentials were passed!");
		}

		UserDto user = userCrudService.findByEmail(credentials.getEmail());
		if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect credentials were passed!");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getEmail(),
						credentials.getPassword()
				)
		);

		List<UserRole> roles = new ArrayList<>();
		if (UserDetailsImpl.adminIds.contains(user.getId())) roles.add(UserRole.ROLE_ADMIN);
		else roles.add(UserRole.ROLE_USER);

		return new SuccessfulAuthDto(
				user.getId(),
				jwtTokenProvider.createToken(credentials.getEmail(), roles)
		);
	}

	public String logout(HttpServletRequest request) {
		try {
			jwtTokenProvider.invalidateToken(request);
		}
		catch(IllegalArgumentException ex)
		{
			return "not valid operation";
		}
		return "done";
	}
}
