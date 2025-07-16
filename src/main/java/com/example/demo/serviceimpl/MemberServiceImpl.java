package com.example.demo.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Friendship;
import com.example.demo.domain.FriendshipStatus;
import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.repository.FriendshipsRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;

import dto.JoinMemberdto;
import dto.MemberWithFriendStatusDto;
import dto.UpdateMemberdto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	private final MemberRepository memRepo;
	
	private final FriendshipsRepository friendshipsRepository;
	 @Override
	 @Transactional
	public ResponseEntity<?> JoinMember(JoinMemberdto dto) {
		  Map<String, Object> response = new HashMap<>();
		  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    String encryptedPassword = passwordEncoder.encode(dto.getPassword());
		    // 회원 생성 및 저장
		    Member member = Member.builder()
		            .nm(dto.getNm())
		            .password(encryptedPassword)
		            .userEmail(dto.getUserEmail())
		            .role(Role.ROLE_MEMBER)
		            .build();
		    memRepo.save(member);

		    response.put("message", "회원가입이 완료되었습니다.");
		    System.out.println(response);

	return ResponseEntity.ok(response);
	}
	 @Override
	 @Transactional
	public ResponseEntity<?> UpdateMember(UpdateMemberdto dto,User authUser){
		
		String email = authUser.getUsername(); // userEmail로 사용
		 Map<String, Object> response = new HashMap<>();
	    Optional<Member> opt = memRepo.findByUserEmail(email);

	    if (!opt.isPresent()) {
	        response.put("message", "해당 사용자를 찾을 수 없습니다.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }

	    Member member = opt.get();

	    boolean isUpdated = false;

	    // 이름 업데이트
	    if (dto.getNm() != null && !dto.getNm().equals(member.getNm())) {
	        member.setNm(dto.getNm());
	        isUpdated = true;
	    }

	    // 비밀번호 업데이트 (암호화)
	    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String encryptedPassword = encoder.encode(dto.getPassword());
	        member.setPassword(encryptedPassword);
	        isUpdated = true;
	    }

	    if (isUpdated) {
	    	memRepo.save(member);
	        response.put("message", "회원 정보가 업데이트되었습니다.");
	    } else {
	        response.put("message", "변경된 정보가 없습니다.");
	    }

	    return ResponseEntity.ok(response);
	}
	
	 public List<MemberWithFriendStatusDto> searchMembersWithFriendStatus(String email, String name, String currentUserEmail) {
		    List<Member> members = memRepo.findByUserEmailContainingAndNmContaining(email, name);
		    // 현재 로그인한 사용자의 Member 엔티티
		    Member me = memRepo.findByUserEmail(currentUserEmail)
		        .orElseThrow(() -> new RuntimeException("현재 사용자 없음"));
		    List<MemberWithFriendStatusDto> result = new ArrayList<>();

		    for (Member other : members) {
		        // 본인은 제외
		        if (other.getId().equals(me.getId())) continue;

		        System.out.println("다른 유저: " + other.getUserEmail());
		        System.out.println("me" +me.getId());
		        System.out.println("other" + other.getId());
		        // 친구 관계 조회 (나 → 상대 or 상대 → 나)
//		        Optional<Friendship> sent = friendshipsRepository.findByMemberAndAddressee(me, other);
//		        Optional<Friendship> received = friendshipsRepository.findByMemberAndAddressee(other, me);
		        Optional<Friendship> sent = friendshipsRepository.findByMemberIdAndAddresseeId(me.getId(), other.getId());
		        Optional<Friendship> received = friendshipsRepository.findByMemberIdAndAddresseeId(other.getId(), me.getId());
//		        Optional<Friendship> test =  friendshipsRepository.findByMemberIdAndAddresseeId(10L, 9L);
		        
//		        System.out.println("test" + test);
		        System.out.println("보낸 관계: " + sent.orElse(null));
		        System.out.println("받은 관계: " + received.orElse(null));
		        Optional<Friendship> friendship = sent.isPresent() ? sent : received;
//		        System.out.println("friendship" + friendship);
		        
		        FriendshipStatus status = friendship.map(Friendship::getStatus).orElse(null);
		        System.out.println("status" +status);
		        MemberWithFriendStatusDto dto = MemberWithFriendStatusDto.builder()
		                .addresseeId(other.getId())
		                .nm(other.getNm())
		                .userEmail(other.getUserEmail())
		                .friendshipStatus(status != null ? status.name() : null)
		                .build();

		        result.add(dto);
		    }
			return result;


		  
		}
	
	
}
