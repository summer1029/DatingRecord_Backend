package dto;

import com.example.demo.domain.FriendshipStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendListDto {

//	    public FriendListDto(Long id2, String nm, String userEmail, FriendshipStatus status2) {
//		// TODO Auto-generated constructor stub
//	}
		private Long id;
	    private String name;
	    private String email;
	    private FriendshipStatus status;
	    private boolean sentByMe;
}
