package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.Friendship;
import com.example.demo.domain.Member;

public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {

	
	Optional<Friendship> findByMemberAndAddressee(Member member, Member addressee);
	
	
	Optional<Friendship> findByAddresseeAndMember(Member addressee, Member member);


	boolean existsByMemberAndAddressee(Member member, Member addressee);


	@Query("select f from Friendship f where f.member.id = :requesterId and f.addressee.id = :addresseeId")
	Optional<Friendship> findByMemberIdAndAddresseeId(@Param("requesterId") Long memberId, @Param("addresseeId") Long addresseeId);

	
	@Query("SELECT f FROM Friendship f WHERE f.member = :member OR f.addressee = :member")
	List<Friendship> findAllByMemberOrAddressee(@Param("member") Member member);


}
