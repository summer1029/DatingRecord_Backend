package dto;

import com.example.demo.domain.WishStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlaceVisitDto {

	private String name;
	private double lat;
	private double lng;
	private WishStatus status;
}
