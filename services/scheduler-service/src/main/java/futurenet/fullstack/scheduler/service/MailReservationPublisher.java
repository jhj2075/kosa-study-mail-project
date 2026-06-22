package futurenet.fullstack.scheduler.service;

import futurenet.fullstack.scheduler.config.MailQueueProperties;
import futurenet.fullstack.scheduler.domain.EmailQueue;
import futurenet.fullstack.scheduler.domain.EmailReservation;
import futurenet.fullstack.scheduler.mapper.SchedulerMailMapper;
import futurenet.fullstack.scheduler.message.MailSendMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailReservationPublisher {

  private final SchedulerMailMapper schedulerMailMapper;
  private final RabbitTemplate rabbitTemplate;
  private final MailQueueProperties mailQueueProperties;

  @Scheduled(fixedDelayString = "${mail-queue.scheduler-fixed-delay-ms:60000}")
  public void publishDueReservations() {
    List<EmailReservation> reservations =
        schedulerMailMapper.findDueWaitingReservations(LocalDateTime.now());

    if (reservations.isEmpty()) {
      log.debug("No due mail reservations found.");
      return;
    }

    log.info("Found {} due mail reservation(s).", reservations.size());
    reservations.forEach(this::publishOne);
  }

  @Transactional
  public void publishOne(EmailReservation reservation) {
    // 예약 메일 상태를 PROCESSING으로 변경
    int processingCount =
        schedulerMailMapper.markProcessing(reservation.getEmailId());

    if (processingCount != 1) {
      log.info(
          "Skipped reservation {} because it is no longer WAITING.",
          reservation.getEmailId()
      );
      return;
    }

    String messageId = UUID.randomUUID().toString();
    MailSendMessage message = MailSendMessage.from(reservation, messageId);
    String payload = message.toJson();

    try {
      // RabbitMQ publish
      rabbitTemplate.convertAndSend(
          mailQueueProperties.exchange(),
          mailQueueProperties.routingKey(),
          payload
      );

      // EMAIL_QUEUE 저장
      EmailQueue queue = EmailQueue.builder()
          .emailId(reservation.getEmailId())
          .exchangeName(mailQueueProperties.exchange())
          .routingKey(mailQueueProperties.routingKey())
          .messageId(messageId)
          .payload(payload)
          .status("PUBLISHED")
          .build();

      schedulerMailMapper.insertQueue(queue);
      schedulerMailMapper.markQueued(reservation.getEmailId()); // 예약 상태를 QUEUED로 변경
      schedulerMailMapper.insertSendLog( // EMAIL_SEND_LOG 저장
          reservation.getEmailId(),
          queue.getQueueId(),
          "QUEUED",
          "Published reservation mail to RabbitMQ"
      );

      log.info(
          "Published reservation {} to RabbitMQ with messageId {}.",
          reservation.getEmailId(),
          messageId
      );
    } catch (RuntimeException exception) { // 실패 시 FAILED 상태와 실패 로그 저장
      schedulerMailMapper.markFailed(reservation.getEmailId());
      schedulerMailMapper.insertSendLog(
          reservation.getEmailId(),
          null,
          "FAILED",
          exception.getMessage()
      );

      log.error(
          "Failed to publish reservation {} to RabbitMQ.",
          reservation.getEmailId(),
          exception
      );
    }
  }
}
