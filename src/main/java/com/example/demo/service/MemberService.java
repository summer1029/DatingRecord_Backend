package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Member;

import dto.JoinMemberdto;
import dto.MemberWithFriendStatusDto;
import dto.UpdateMemberdto;


public interface MemberService {
	
	
	 public ResponseEntity<?> JoinMember(JoinMemberdto dto);

	public ResponseEntity<?> UpdateMember(UpdateMemberdto dto,User authUser);

	public List<MemberWithFriendStatusDto> searchMembersWithFriendStatus(String email, String name,
			String currentEmail);

	 
		
		
		
}


