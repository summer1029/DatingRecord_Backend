package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.domain.Member;
import com.example.demo.domain.Role;


@SpringBootTest
public class MemberInitialize {

	
		
		@Autowired
		private com.example.demo.repository.MemberRepository memRepo;
		@Autowired
		private PasswordEncoder encoder;

		@Test
		public void memberInitialize() {
	
		
		memRepo.save(Member.builder()
				.userEmail("member")
				.password(encoder.encode("abcd"))
				.role(Role.ROLE_MEMBER)
				.build());
		
		memRepo.save(Member.builder()
				.userEmail("manager")
				.password(encoder.encode("abcd"))
				.role(Role.ROLE_MANAGER)
				.build());
		memRepo.save(Member.builder()
				.userEmail("admin")
				.password(encoder.encode("abcd"))
				.role(Role.ROLE_ADMIN)
				.build());
		}
		}
