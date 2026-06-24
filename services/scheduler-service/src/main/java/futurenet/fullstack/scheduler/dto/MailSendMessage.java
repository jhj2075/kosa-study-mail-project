package futurenet.fullstack.scheduler.dto;

import futurenet.fullstack.scheduler.domain.EmailReservation;
import java.time.LocalDateTime;

public record MailSendMessage(
    Long emailId,
    Long userId,
    String subject,
    String content,
    String receiverEmail,
    LocalDateTime reserveDatetime,
    String messageId
) {

  public static MailSendMessage from(
      EmailReservation reservation,
      String messageId
  ) {
    return new MailSendMessage(
        reservation.getEmailId(),
        reservation.getUserId(),
        reservation.getSubject(),
        reservation.getContent(),
        reservation.getReceiverEmail(),
        reservation.getReserveDatetime(),
        messageId
    );
  }
}
