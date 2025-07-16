package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class RequestEventdto {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
