package dto;

import com.example.demo.domain.FriendshipStatus;
import com.example.demo.domain.WishStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceUpdateDto {
	  private String visitedNm;
	  private WishStatus status;
}
