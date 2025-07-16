package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.PlaceVisit;
import com.example.demo.domain.WishStatus;

public interface PlaceVisitRepository extends JpaRepository<PlaceVisit, Long> {

	List<PlaceVisit> findByMember_UserEmailAndStatusNot(String username, WishStatus deleted);

}
