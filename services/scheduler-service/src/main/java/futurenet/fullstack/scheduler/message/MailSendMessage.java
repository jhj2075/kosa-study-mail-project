package futurenet.fullstack.scheduler.message;

import futurenet.fullstack.scheduler.domain.EmailReservation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MailSendMessage(
    Long emailId,
    Long userId,
    String subject,
    String content,
    String receiverEmail,
    String reserveDatetime,
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
        format(reservation.getReserveDatetime()),
        messageId
    );
  }

  public String toJson() {
    return "{"
        + "\"emailId\":" + emailId + ","
        + "\"userId\":" + userId + ","
        + "\"subject\":\"" + escape(subject) + "\","
        + "\"content\":\"" + escape(content) + "\","
        + "\"receiverEmail\":\"" + escape(receiverEmail) + "\","
        + "\"reserveDatetime\":\"" + escape(reserveDatetime) + "\","
        + "\"messageId\":\"" + escape(messageId) + "\""
        + "}";
  }

  private static String format(LocalDateTime value) {
    if (value == null) {
      return "";
    }

    return value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  private static String escape(String value) {
    if (value == null) {
      return "";
    }

    return value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\r", "\\r")
        .replace("\n", "\\n")
        .replace("\t", "\\t");
  }
}
