package dto;

import java.time.LocalDateTime;

import com.example.demo.domain.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

	private Long id;
	private String title;
	private LocalDateTime start;
	private LocalDateTime end;
	private String color;
	private Boolean isDelete;
	public EventDto(Event event) {
	    this.id = event.getId();
	    this.title = event.getTitle();
	    this.start = event.getStartDateTime();
	    this.end = event.getEndDateTime();
	    this.color=event.getColor();
	    this.isDelete=event.getIsDelete();
	}
	public static EventDto from(Event event) {
	    return new EventDto(event);
	}

}
