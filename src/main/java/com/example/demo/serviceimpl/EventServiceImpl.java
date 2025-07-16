package com.example.demo.serviceimpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Event;
import com.example.demo.domain.Member;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.EventService;

import dto.RequestEventdto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService{

	private final EventRepository eventRepository;
	
	public void createSchedule(RequestEventdto dto, Member member) {
	    Event schedule = new Event();
        schedule.setTitle(dto.getTitle());
        schedule.setStartDateTime(dto.getStartDate());
        schedule.setEndDateTime(dto.getEndDate());
        schedule.setMember(member);
        schedule.setColor(getRandomColor());
        eventRepository.save(schedule);
     
		
	}
	
	

	private String getRandomColor() {
	    List<String> colors = List.of(
	        "#FF5733", "#33FF57", "#3357FF", "#F39C12", "#9B59B6",
	        "#1ABC9C", "#E74C3C", "#34495E", "#2ECC71", "#3498DB",
	        "#E67E22", "#BDC3C7", "#7F8C8D", "#C0392B", "#8E44AD"
	    );

	    if (colors.isEmpty()) {
	        return "#000000"; // fallback 기본값
	    }

	    Random random = new Random();
	    return colors.get(random.nextInt(colors.size()));
	}



	
	public List<Event> findByYearAndMember(Member member, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return eventRepository.findByYearAndMember(member, startDateTime, endDateTime);
		} catch (Exception e) {
			System.err.println("이벤트 조회 중 오류 발생: " + e.getMessage());
	        return Collections.emptyList();
		}
		
	}
}
