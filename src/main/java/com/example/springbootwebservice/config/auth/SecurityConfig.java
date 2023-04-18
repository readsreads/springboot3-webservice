package com.example.springbootwebservice.config.auth;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.example.springbootwebservice.domain.user.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toH2Console())
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
            	.authorizeHttpRequests()
            		.requestMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
            		.requestMatchers("/api/v1/**").hasRole(Role.USER.name())
            		.requestMatchers("").authenticated()
			.and()
            	.logout().logoutSuccessUrl("/")
            .and()
            	.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
	}
}
