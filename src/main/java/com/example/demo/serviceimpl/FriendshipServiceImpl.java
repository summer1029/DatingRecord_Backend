package com.example.demo.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Friendship;
import com.example.demo.domain.FriendshipStatus;
import com.example.demo.domain.Member;
import com.example.demo.repository.FriendshipsRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.FriendshipsService;

import dto.FriendListDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipsService {

	private final FriendshipsRepository friendshipRepository;
	
	private final MemberRepository memberRepo;
	
	public void sendFriendRequest(Member requester, Member addressee) {
        if (requester.getId().equals(addressee.getId())) {
            throw new IllegalArgumentException("자기 자신에게 요청할 수 없습니다.");
        }

        friendshipRepository.findByMemberAndAddressee(requester, addressee)
            .ifPresent(f -> {
                throw new IllegalStateException("이미 친구 요청 또는 관계가 존재합니다.");
            });

        Friendship friendship = Friendship.builder()
                .member(requester)
                .addressee(addressee)
                .status(FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friendship);
    }
	
	
	public void acceptFriendRequest(Member requester,Member addressee) {
	   
	    	
	    	System.out.println("requester = " + requester);     // null 아니어야 함
	    	System.out.println("addressee = " + addressee);
	    	Friendship friendship = friendshipRepository
		            .findByMemberAndAddressee(requester, addressee)
		            .orElseThrow(() -> new IllegalStateException("친구 요청이 존재하지 않습니다."));

		    if (friendship.getStatus() != FriendshipStatus.PENDING) {
		        throw new IllegalStateException("이미 처리된 요청입니다.");
		    }
		    friendship.setStatus(FriendshipStatus.ACCEPTED);
		    friendshipRepository.save(friendship);
		
	}
	@Transactional
	public void deleteFriend(Member me, Member target) {
	    Friendship friendship = friendshipRepository.findByMemberAndAddressee(me, target)
	        .or(() -> friendshipRepository.findByMemberAndAddressee(me, target))
	        .orElseThrow(() -> new IllegalStateException("친구 관계가 존재하지 않습니다."));

	    friendship.setStatus(FriendshipStatus.DELETED);
	    friendshipRepository.save(friendship);
	}
	
	public List<FriendListDto> getMyFriends(String email) {
	    Member me = memberRepo.findByUserEmail(email)
	        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

	    List<Friendship> relations = friendshipRepository.findAllByMemberOrAddressee(me);	
	    System.out.println(relations);
//	    return null;
	    return relations.stream()
	        .map(f -> {
	            Member friend = f.getMember().equals(me) ? f.getAddressee() : f.getMember();
	            boolean sentByMe = f.getMember().equals(me);
	            return new FriendListDto(friend.getId(), friend.getNm(), friend.getUserEmail(), f.getStatus(),sentByMe);
	        })
	        .collect(Collectors.toList());
	}


}
