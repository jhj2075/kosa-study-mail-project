package futurenet.fullstack.scheduler.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailReservation {

  private Long emailId;
  private Long userId;
  private String subject;
  private String content;
  private String receiverEmail;
  private LocalDateTime reserveDatetime;
}
