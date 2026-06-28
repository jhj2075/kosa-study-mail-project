package futurenet.fullstack.mailworker.domain;

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
  private String status;
}
