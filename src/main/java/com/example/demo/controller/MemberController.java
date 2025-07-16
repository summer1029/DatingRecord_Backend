package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;

import dto.DuplCheckDTO;
import dto.JoinMemberdto;
import dto.MemberWithFriendStatusDto;
import dto.UpdateMemberdto;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://172.30.1.78:3000:3000") 
@RequiredArgsConstructor
@RestController
public class MemberController {
		

	
	private final MemberRepository memRepo;
	
	private final MemberService ms;
	
		@PostMapping ("duplcheck")
		public ResponseEntity<Map<String, Object>> checkDuplicate(@RequestBody DuplCheckDTO dto) {
		   String userEmail = dto.getUserEmail();
			Optional<Member> findMember = memRepo.findByUserEmail(userEmail);
		   
		   // 응답용 Map 객체 생성
		    Map<String, Object> response = new HashMap<>();
		   // Optional<Member>가 비어있지 않으면 (즉, 중복된 사용자 있으면)
		    if (findMember.isPresent()) {
		        // 중복된 사용자 메시지
		        response.put("isDuplicate", true);
		        response.put("message", "중복된 이메일입니다.");
		    } else {
		        // 사용 가능한 사용자 메시지
		        response.put("isDuplicate", false);
		        response.put("message", "사용 가능한 이메일입니다.");
		    }
		    return ResponseEntity.ok(response);
		}

		@PostMapping("/join")
		public ResponseEntity<?> joinMember(@RequestBody JoinMemberdto dto) {

			   System.out.println("Join 요청 들어옴: " + dto);

			Map<String, Object> response = new HashMap<>();
			 Optional<Member> existingMember = memRepo.findByUserEmail(dto.getUserEmail());
			    if (existingMember.isPresent()) {
			        // 중복된 사용자 처리
			        response.put("message", "중복된 이메일입니다.");
			        
			        return ResponseEntity.badRequest().body(response);
			    }else {
			    	System.out.println("dddddddddddd");
			    	return ms.JoinMember(dto);
			    }

			  
			    
		}
//		
		@PutMapping("/update")
		public ResponseEntity<?> updateMember (@RequestBody UpdateMemberdto dto,	@AuthenticationPrincipal User authUser			) {
			System.out.println(dto);
			System.out.println("authuser" + authUser);		
			Map<String, Object> response = new HashMap<>();
			if(authUser == null) {
				 response.put("message", "권한이 없습니다 .");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}else {
				return ms.UpdateMember(dto, authUser);
			}
	        
		}
			@GetMapping("/list")
			public ResponseEntity<?> searchMembers(
			        @RequestParam(value = "email", defaultValue = "") String email,
			        @RequestParam(value = "name",  defaultValue = "") String name,
			        @AuthenticationPrincipal User authUser) {
				Map<String, Object> response = new HashMap<>();
			    try {
			        String currentEmail = authUser.getUsername();
			        List<MemberWithFriendStatusDto> result =
			            ms.searchMembersWithFriendStatus(email, name, currentEmail);
	
			        return ResponseEntity.ok(result);
			       
	
			    } catch (Exception e) {
			        e.printStackTrace();
			        response.put("message", "유저 조회에 실패했습니다 .");
			        return ResponseEntity
			            .status(HttpStatus.INTERNAL_SERVER_ERROR)
			            .body(response);
			    }
			    }
		
		@GetMapping("/myinfo")
		public ResponseEntity<?> getMyInfo (@AuthenticationPrincipal User user){
			Map<String, Object> response = new HashMap<>();
			try {
				String email = user.getUsername();
				Member info = memRepo.findByUserEmail(email)
		                .orElseThrow(() -> new RuntimeException("info not found"));
				return ResponseEntity.ok(info);
			} catch (Exception e) {
				e.printStackTrace();
				 response.put("message", "유저정보 조회에 실패했습니다.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
		
		
			
			
			
		}

		    


}

