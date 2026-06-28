package futurenet.fullstack.mailworker.dto;

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
}
