package com.example.demo.config;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	
//	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//		// /login으로 요청이 왔을 때만 이 필터가 작동하게 지정
//		setFilterProcessesUrl("/login");
//	}
	@Override
	public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException {
		// request에서 json 타입의 [username/password]를 읽어서 Member 객체를 생성한다.
		ObjectMapper mapper = new ObjectMapper();
		Member member = null;
		try {
		member = mapper.readValue(request.getInputStream(), Member.class);
		} catch (Exception e) {
		e.printStackTrace();
		}
		// Security에게 로그인 요청에 필요한 객체 생성
		if (member == null || member.getUserEmail() == null || member.getPassword() == null) {
		    throw new RuntimeException("잘못된 로그인 요청입니다. (member 혹은 필드 null)");
		}
		Authentication authToken = new UsernamePasswordAuthenticationToken(member.getUserEmail(),
		member.getPassword());
		// 인증 진행-> UserDetailsService를 상속받은 클래스의 loadUserByUsername 호출한다.
		Authentication auth = authenticationManager.authenticate(authToken);
		System.out.println("auth:" + auth);
		return auth;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		User user = (User)authResult.getPrincipal();
		String token = JWT.create()
		.withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10*10000))
		.withClaim("username", user.getUsername())
		.sign(Algorithm.HMAC256("edu.pnu.jwt"));
		response.addHeader("Authorization", "Bearer " + token);
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    ObjectMapper objectMapper = new ObjectMapper();
	    LoginResponse loginResponse = new LoginResponse("로그인 성공", token, user.getUsername());
	    response.getWriter().write(objectMapper.writeValueAsString(loginResponse));

	    
	

	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
	    // 실패 응답 설정
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 상태 코드
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    // 실패 메시지 생성
	    ObjectMapper objectMapper = new ObjectMapper();
	    LoginErrorResponse errorResponse = new LoginErrorResponse("로그인 실패", failed.getMessage());
	    
	    // JSON 응답 전송
	    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
	
	

	// 실패 응답 객체 클래스
	static class LoginErrorResponse {
	    private String message;
	    private String error;

	    public LoginErrorResponse(String message, String error) {
	        this.message = message;
	        this.error = error;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public String getError() {
	        return error;
	    }

	    public void setError(String error) {
	        this.error = error;
	    }
	}


	@Getter @Setter @AllArgsConstructor
	public class LoginResponse {
	    private String message;
	    private String token;
	    private String username;
	}
	
}
