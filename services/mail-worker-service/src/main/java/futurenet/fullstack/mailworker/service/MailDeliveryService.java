package futurenet.fullstack.mailworker.service;

import futurenet.fullstack.mailworker.config.MailSenderProperties;
import futurenet.fullstack.mailworker.domain.EmailReservation;
import futurenet.fullstack.mailworker.dto.MailSendMessage;
import futurenet.fullstack.mailworker.mapper.MailWorkerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailDeliveryService {

  private final MailWorkerMapper mailWorkerMapper;
  private final JavaMailSender javaMailSender;
  private final MailSenderProperties mailSenderProperties;

  @Transactional
  public void deliver(MailSendMessage message) {
    Long queueId = mailWorkerMapper.findQueueIdByMessageId(message.messageId());
    EmailReservation reservation = mailWorkerMapper.findReservationByEmailId(message.emailId());

    if (reservation == null) {
      log.warn("Skipped mail message {} because reservation {} was not found.",
          message.messageId(),
          message.emailId()
      );
      return;
    }

    int sendingCount = mailWorkerMapper.markSending(message.emailId());

    if (sendingCount != 1) {
      if (isPublishTransactionNotVisibleYet(reservation.getStatus())) {
        throw new IllegalStateException(
            "Reservation " + message.emailId() + " is not QUEUED yet. currentStatus="
                + reservation.getStatus()
        );
      }

      log.info("Skipped reservation {} because it is not QUEUED. currentStatus={}",
          message.emailId(),
          reservation.getStatus()
      );
      return;
    }

    mailWorkerMapper.insertSendLog(
        message.emailId(),
        queueId,
        "SENDING",
        "Started sending reservation mail"
    );

    try {
      sendMail(reservation);
      mailWorkerMapper.markSent(message.emailId());

      if (queueId != null) {
        mailWorkerMapper.markQueueConsumed(queueId);
      }

      mailWorkerMapper.insertSendLog(
          message.emailId(),
          queueId,
          "SENT",
          "Sent reservation mail"
      );

      log.info("Sent reservation mail. emailId={}, messageId={}",
          message.emailId(),
          message.messageId()
      );
    } catch (MailException exception) {
      mailWorkerMapper.markFailed(message.emailId());

      if (queueId != null) {
        mailWorkerMapper.markQueueFailed(queueId);
      }

      mailWorkerMapper.insertSendLog(
          message.emailId(),
          queueId,
          "FAILED",
          trimMessage(exception.getMessage())
      );

      log.error("Failed to send reservation mail. emailId={}, messageId={}",
          message.emailId(),
          message.messageId(),
          exception
      );
    }
  }

  private void sendMail(EmailReservation reservation) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(mailSenderProperties.from());
    mailMessage.setTo(reservation.getReceiverEmail());
    mailMessage.setSubject(reservation.getSubject());
    mailMessage.setText(StringUtils.hasText(reservation.getContent())
        ? reservation.getContent()
        : ""
    );

    javaMailSender.send(mailMessage);
  }

  private boolean isPublishTransactionNotVisibleYet(String status) {
    return "WAITING".equals(status) || "PROCESSING".equals(status) || "QUEUED".equals(status);
  }

  private String trimMessage(String message) {
    if (message == null) {
      return "Mail send failed";
    }

    if (message.length() <= 1000) {
      return message;
    }

    return message.substring(0, 1000);
  }
}
