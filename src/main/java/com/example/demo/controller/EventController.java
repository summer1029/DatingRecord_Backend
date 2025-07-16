package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Event;
import com.example.demo.domain.Member;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.EventService;

import dto.EventDto;
import dto.RequestEventdto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventController {

	private final EventRepository eventRepository;
	private final MemberRepository memberRepository;
	private final EventService eventService; 
	
	@GetMapping("/events")
	public ResponseEntity<List<EventDto>> getEventsByYear(
	    @AuthenticationPrincipal User user,  // 또는 CustomUserDetails
	    @RequestParam("startYear") Long startYear,
	    @RequestParam(value="endYear",required = false) Long endYear
	) {
		Member member = memberRepository.findByUserEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

		  if (endYear == null) {
		        endYear = startYear;
		    }
	    LocalDateTime startDateTime = LocalDate.of(startYear.intValue(), 1, 1).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.of(endYear.intValue(), 12, 31).atTime(23, 59, 59);
	    List<Event> events = eventService.findByYearAndMember( member,startDateTime,endDateTime);
	    List<EventDto> dtoList = events.stream()
	                                   .map(EventDto::from)
	                                   .toList();

	    return ResponseEntity.ok(dtoList);
	}

	@PostMapping("add/schedule")
	public ResponseEntity<?> createSchedule(@RequestBody RequestEventdto dto,
	                                        @AuthenticationPrincipal User user) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	 if (dto.getStartDate() == null) {
	             response.put("error", "시작일은 필수입니다.");
	             return ResponseEntity.badRequest().body(response); // 400 반환
	         }
	    	 if (dto.getTitle() == null) {
	             response.put("error", "내용을 입력해주세.");
	             return ResponseEntity.badRequest().body(response); // 400 반환
	         }
	        Member member = memberRepository.findByUserEmail(user.getUsername())
	                .orElseThrow(() -> new RuntimeException("유저 없음"));

	        eventService.createSchedule(dto, member);
	        response.put("message", "일정 등록 완료");
	        
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("error", "일정 등록 실패");
	        response.put("details", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@PutMapping("/update/schedule/{eventId}")
	public ResponseEntity<?> updateSchedule(@PathVariable("eventId") Long eventId,
	                                        @RequestBody RequestEventdto dto,
	                                        @AuthenticationPrincipal User user) {
	    Map<String, Object> response = new HashMap<>();
	    System.out.println(dto);
	    try {
	        // 사용자 조회
	        Member member = memberRepository.findByUserEmail(user.getUsername())
	                .orElseThrow(() -> new RuntimeException("유저 없음"));

	        // 일정 조회
	        Event schedule = eventRepository.findById(eventId)
	                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));

	        // 본인 일정인지 확인
	        if (!schedule.getMember().getId().equals(member.getId())) {
	            response.put("error", "본인 일정만 수정할 수 있습니다");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }
	        if(dto.getTitle() != null) {
	        	 schedule.setTitle(dto.getTitle());
	        }
	        if(dto.getStartDate() != null) {
	        	schedule.setStartDateTime(dto.getStartDate());
	        }
	        if(dto.getEndDate() != null) {
	        	schedule.setEndDateTime(dto.getEndDate());
	        }
	       
	        
	      

	        eventRepository.save(schedule);

	        response.put("message", "일정 수정 완료");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("error", "일정 수정 실패");
	        response.put("details", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }      
	}

	
	@PutMapping("/delete/schedule/{eventId}")
	public ResponseEntity<?> deleteSchedule(@PathVariable("eventId") Long eventId,
	                                        @AuthenticationPrincipal User user) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // 로그인 사용자 확인
	        Member member = memberRepository.findByUserEmail(user.getUsername())
	                .orElseThrow(() -> new RuntimeException("유저 없음"));

	        // 삭제 대상 일정 조회
	        Event schedule = eventRepository.findById(eventId)
	                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));

	        // 본인의 일정인지 확인
	        if (!schedule.getMember().getId().equals(member.getId())) {
	            response.put("error", "본인 일정만 삭제할 수 있습니다");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }

	        // 삭제 실패 
	        schedule.setIsDelete(true);
	        eventRepository.save(schedule);
	        response.put("message", "일정 삭제 완료");
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("error", "일정 삭제 실패");
	        response.put("details", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	

	
}
