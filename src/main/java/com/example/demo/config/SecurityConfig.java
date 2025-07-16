package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.repository.MemberRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
	}
	@Autowired
	private AuthenticationConfiguration auth;
	@Autowired
	private MemberRepository memberRepository;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
//		http.
//		authorizeHttpRequests(security->security
//				 .requestMatchers(new AntPathRequestMatcher("/join")).permitAll()
//				.requestMatchers(new AntPathRequestMatcher("/member/**")).authenticated()
//				.requestMatchers(new AntPathRequestMatcher("/manager/**")).hasRole("MANAGER")
//				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
//				.anyRequest().permitAll());
		
		 http
	        .cors(cors -> {}) 
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(security -> security
	        	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	        	.requestMatchers(new AntPathRequestMatcher("/member/**")).authenticated()
	            .requestMatchers(new AntPathRequestMatcher("/manager/**")).hasRole("MANAGER")
	            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
	            .anyRequest().permitAll()
	        )
	        .exceptionHandling(exception -> exception
	        	    .authenticationEntryPoint((request, response, authException) -> {
	        	        response.setContentType("application/json;charset=UTF-8");
	        	        response.setStatus(401);
	        	        String json = "{\"status\":401, \"error\":\"Unauthorized\", \"message\":\"인증이 필요합니다.\"}";
	        	        response.getWriter().write(json);
	        	    })
	        	    .accessDeniedHandler((request, response, accessDeniedException) -> {
	        	        response.setContentType("application/json;charset=UTF-8");
	        	        response.setStatus(403);
	        	        String json = "{\"status\":403, \"error\":\"Forbidden\", \"message\":\"권한이 없습니다.\"}";
	        	        response.getWriter().write(json);
	        	    })
	        	);

				
		 		
		http.formLogin(frmLogin->frmLogin.disable());
		http.httpBasic(basic->basic.disable()); 
		http.addFilter(new JWTAuthenticationFilter(
		auth.getAuthenticationManager()));
		http.addFilterBefore(new JWTAuthorizationFilter(memberRepository), AuthorizationFilter.class);
				
		return http.build();
		//127.0.0.1 내 주소 
	}
}
