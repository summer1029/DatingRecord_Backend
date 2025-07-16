package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 허용
                .allowedOrigins(
//                		"*"
                		"http://localhost:3000",
                		 "https://e7e1-61-76-141-85.ngrok-free.app" 
//                		"http://localhost:8081",
//                		"http://172.30.1.29:8081",
//                		"http://172.30.1.78:3000",
//                		"http://localhost:3000"
                		) // React Native 클라이언트의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .exposedHeaders("Authorization")
                .allowCredentials(false); // 쿠키나 인증 정보 허용
    }
}
