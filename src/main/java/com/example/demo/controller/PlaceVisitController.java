package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Member;
import com.example.demo.domain.PlaceVisit;
import com.example.demo.domain.WishStatus;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PlaceVisitRepository;

import dto.PlaceUpdateDto;
import dto.PlaceVisitDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceVisitController {
	
	private final MemberRepository memberRepo;
	
	private final PlaceVisitRepository placeVisitRepository;

	@PostMapping("/add/place")
	public ResponseEntity<?> addPlace(@RequestBody PlaceVisitDto dto, @AuthenticationPrincipal User user) {
	  
		 Map<String, Object> response = new HashMap<>();
	    try {
	    	
	    	  Member member = memberRepo.findByUserEmail(user.getUsername())
	    		        .orElseThrow(() -> new RuntimeException("유저 정보 없음"));
	    		   
	    		    PlaceVisit place = PlaceVisit.builder()
	    		        .visitedNm(dto.getName())
	    		        .lat(dto.getLat())
	    		        .lng(dto.getLng())
	    		        .status(dto.getStatus())
	    		        .member(member)
	    		        .build();

	    		    placeVisitRepository.save(place);
	    		    response.put("message", "장소 저장 완료  ");
	    		    return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", "장소 저장 실패  ");
	        return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping("/place/{id}")
	public ResponseEntity<?> markAsVisited(@PathVariable("id") Long id,@RequestBody PlaceUpdateDto dto, @AuthenticationPrincipal User user) {
		Map<String, Object> response = new HashMap<>();
		try {
	        PlaceVisit place = placeVisitRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("장소 없음"));

	        // 🔐 유저 인증 체크 (필요 시 주석 해제)
	        // if (!place.getMember().getUserEmail().equals(user.getUsername())) {
	        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
	        // }

	        if (dto.getVisitedNm() != null) {
	            place.setVisitedNm(dto.getVisitedNm());
	        }

	        if (dto.getStatus() != null) {
	            place.setStatus(dto.getStatus());
	        }

	        placeVisitRepository.save(place);

	        response.put("message", "장소가 성공적으로 수정되었습니다.");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("message", "수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
		
	}
	
	@DeleteMapping("/place/{id}")
	public ResponseEntity<?> softDelete(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
		Map<String, Object> response = new HashMap<>();
	    try {
	    	  PlaceVisit place = placeVisitRepository.findById(id)
	    		        .orElseThrow(() -> new RuntimeException("장소 없음"));
	    		   

	    		    if (!place.getMember().getUserEmail().equals(user.getUsername())) {
	    		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한 없음");
	    		    }

	    		    place.setStatus(WishStatus.DELETED);
	    		    placeVisitRepository.save(place);
	    		    response.put("message","장소 삭제 성공 ");
	    		    return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message","장소 삭제 실패 ");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/places")
	public ResponseEntity<?> getAllMyPlaces(@AuthenticationPrincipal User user) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PlaceVisit> list = placeVisitRepository.findByMember_UserEmailAndStatusNot(user.getUsername(), WishStatus.DELETED);
		    return ResponseEntity.ok(list);
		} catch (Exception e) {
			response.put("message","장소 조회 실패 ");
			return ResponseEntity.badRequest().body(response);
		}
		
	}
}
