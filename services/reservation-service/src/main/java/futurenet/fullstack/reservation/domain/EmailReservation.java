package futurenet.fullstack.reservation.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailReservation {

  private Long emailId;
  private Long userId;
  private String subject;
  private String content;
  private String receiverEmail;
  private LocalDateTime reserveDatetime;
  private LocalDateTime sentDatetime;
  private ReservationStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}