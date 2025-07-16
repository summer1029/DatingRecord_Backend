package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Member;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private com.example.demo.repository.MemberRepository memRepo;
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		// memRepo에서 사용자 정보를 검색해서
		Member member = memRepo.findByUserEmail(userEmail)
		.orElseThrow(()->new UsernameNotFoundException("Not Found"));
		System.out.println(member); // 검색된 사용자 정보를 console에 출력해서 확인
		// UserDetails 타입의 객체를 생성해서 리턴 (o.s.s.core.userdetails.User)
		return new User(member.getUserEmail() ,member.getPassword(),
				AuthorityUtils.createAuthorityList(member.getRole().toString()));
	}

}
