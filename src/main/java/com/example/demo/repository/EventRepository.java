package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Event;
import com.example.demo.domain.Member;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("""
			  SELECT e FROM Event e
			  WHERE e.member = :member
			    AND (
			      e.startDateTime <= :endOfYear
			      AND (e.endDateTime IS NULL OR e.endDateTime >= :startOfYear)
			    )
			""")
			List<Event> findByYearAndMember(
			  @Param("member") Member member,
			  @Param("startOfYear") LocalDateTime start,
			  @Param("endOfYear") LocalDateTime end
			);


	

}
