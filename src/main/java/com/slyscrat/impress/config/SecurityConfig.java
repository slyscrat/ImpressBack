package com.slyscrat.impress.config;

import com.slyscrat.impress.service.security.jwt.JwtTokenFilterConfigurer;
import com.slyscrat.impress.service.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true,
		jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private JwtTokenProvider jwtTokenProvider;
	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";

	@Autowired
	public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:8080")
						.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
			}
		};
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.cors()
				.and()
				.authorizeRequests()

				.antMatchers("/movie/list").permitAll()
				.antMatchers("/movie/test").permitAll()
				.antMatchers("/movie/{\\d+}").permitAll()
				.antMatchers("/movie/{\\d+}/note").hasAnyRole(ADMIN, USER)
				.antMatchers("/movie/{\\d+}/rate").hasAnyRole(ADMIN, USER)
				.antMatchers("/movie/{\\d+}/edit").hasRole(ADMIN)
				.antMatchers("/movie/{\\d+}/delete").hasRole(ADMIN)
				.antMatchers("/movie/list/recommended").hasAnyRole(ADMIN, USER)
				.antMatchers("/movie/list/rated").hasAnyRole(ADMIN, USER)
				.antMatchers("/movie/list/future").hasAnyRole(ADMIN, USER)

				.antMatchers("/game/list").permitAll()
				.antMatchers("/game/test").permitAll()
				.antMatchers("/game/{\\d+}").permitAll()
				.antMatchers("/game/{\\d+}/note").hasAnyRole(ADMIN, USER)
				.antMatchers("/game/{\\d+}/rate").hasAnyRole(ADMIN, USER)
				.antMatchers("/game/{\\d+}/edit").hasRole(ADMIN)
				.antMatchers("/game/{\\d+}/delete").hasRole(ADMIN)
				.antMatchers("/game/list/recommended").hasAnyRole(ADMIN, USER)
				.antMatchers("/game/list/rated").hasAnyRole(ADMIN, USER)
				.antMatchers("/game/list/future").hasAnyRole(ADMIN, USER)

				.antMatchers("/book/list").permitAll()
				.antMatchers("/book/test").permitAll()
				.antMatchers("/book/{\\d+}").permitAll()
				.antMatchers("/book/{\\d+}/note").hasAnyRole(ADMIN, USER)
				.antMatchers("/book/{\\d+}/rate").hasAnyRole(ADMIN, USER)
				.antMatchers("/book/{\\d+}/edit").hasRole(ADMIN)
				.antMatchers("/book/{\\d+}/delete").hasRole(ADMIN)
				.antMatchers("/book/list/recommended").hasAnyRole(ADMIN, USER)
				.antMatchers("/book/list/rated").hasAnyRole(ADMIN, USER)
				.antMatchers("/book/list/future").hasAnyRole(ADMIN, USER)

				.antMatchers("/auth/login").permitAll()
				.antMatchers("/auth/registration").permitAll()
				.antMatchers("/auth/logout").hasAnyRole(ADMIN, USER)

				.antMatchers("/user/{id}").hasAnyRole(ADMIN, USER)
				.antMatchers("/user/{id}/del").hasRole(ADMIN)
				.antMatchers("/user/{id}/edit").hasRole(ADMIN)
				.antMatchers("/user/list").hasRole(ADMIN)

				.and()
				.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
		http.requiresChannel()
				.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
				.requiresSecure();
	}
}
