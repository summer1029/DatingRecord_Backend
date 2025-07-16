package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.FriendshipsService;

import dto.FriendListDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FriendshipsController {


	
	private final FriendshipsService friendshipService;
	private final MemberRepository msmRepo;
    // 친구 요청
	@PostMapping("/invite/{targetId}")
	public ResponseEntity<?> sendRequest(@AuthenticationPrincipal User requester,
			@PathVariable("targetId") Long targetId) {
		 Map<String, Object> response = new HashMap<>();
		try {
	    	
	    	 
	        String email = requester.getUsername();
	      
	        Member requesterMember = msmRepo.findByUserEmail(email)
	                .orElseThrow(() -> new RuntimeException("Requester not found"));

	        Member addressee = msmRepo.findById(targetId)
	                .orElseThrow(() -> new RuntimeException("Addressee not found"));

	      
	        friendshipService.sendFriendRequest(requesterMember, addressee);
	        response.put("message", "친구 요청  완료 ");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	    	 response.put("message", "친구 요청 실 ");
	        return ResponseEntity.badRequest().body(response);
	    }
	}


    // 친구 수락
	@PutMapping("/accept/{requesterId}")
	public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal User addresseee,
	                                       @PathVariable("requesterId") Long requesterId) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        String email = addresseee.getUsername();
	        
	        Member requester = msmRepo.findById(requesterId)
	            .orElseThrow(() -> new IllegalArgumentException("요청한 사용자를 찾을 수 없습니다."));
	        Member addressee = msmRepo.findByUserEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다."));

	        System.out.println("addressee = " + addressee);
	        
	        friendshipService.acceptFriendRequest(requester, addressee);
	        response.put("message", "요청 수락 완료");
	        return ResponseEntity.ok(response);
	        
	    } catch (IllegalArgumentException e) {
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    } catch (Exception e) {
	        e.printStackTrace();  // 개발 중에는 남겨두고, 운영에서는 로깅 처리 추천
	        response.put("error", "서버 오류 발생");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
    // 친구 삭제
    @PutMapping("/delete/{targetId}")
    public ResponseEntity<?> deleteFriend(@AuthenticationPrincipal User itme,
                                          @PathVariable("targetId") Long targetId) {
    	System.out.println(itme);
    	 Map<String, Object> response = new HashMap<>();
    	String email = itme.getUsername();
    	Member requester = msmRepo.findById(targetId)
    	        .orElseThrow(() -> new IllegalArgumentException("요청한 사용자를 찾을 수 없습니다."));
    	Member addressee = msmRepo.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다."));

        friendshipService.deleteFriend(requester, addressee);
        response.put("message", "친구 삭제 완료");
        return ResponseEntity.ok(response);
    }

	@GetMapping("/friends")
	public ResponseEntity<?> getMyFriends(@AuthenticationPrincipal User authUser) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        String myEmail = authUser.getUsername(); // 토큰에서 이메일 추출
	        List<FriendListDto> friends = friendshipService.getMyFriends(myEmail);
	        return ResponseEntity.ok(friends);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "친구 목록 불러오기 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
}
