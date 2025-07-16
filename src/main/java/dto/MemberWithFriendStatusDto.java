package dto;


import com.example.demo.domain.FriendshipStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberWithFriendStatusDto {
    private Long addresseeId;
    private String nm;
    private String userEmail;
    private Long friendshipId;            // 요청 레코드 ID
    private String friendshipStatus; 
}
