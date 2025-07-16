package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.domain.Event;
import com.example.demo.domain.Member;

import dto.RequestEventdto;


public interface EventService {

	
	void createSchedule(RequestEventdto dto, Member member);

	List<Event> findByYearAndMember(Member member, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
