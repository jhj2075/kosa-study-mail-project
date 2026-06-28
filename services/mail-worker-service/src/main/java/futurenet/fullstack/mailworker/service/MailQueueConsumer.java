package futurenet.fullstack.mailworker.service;

import futurenet.fullstack.mailworker.dto.MailSendMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailQueueConsumer {

  private final MailDeliveryService mailDeliveryService;
  private final JsonMapper jsonMapper;

  @RabbitListener(queues = "${mail-queue.queue}")
  public void consume(byte[] body) {
    MailSendMessage message = parseMessage(body);
    log.info("Received mail send message. emailId={}, messageId={}",
        message.emailId(),
        message.messageId()
    );
    mailDeliveryService.deliver(message);
  }

  private MailSendMessage parseMessage(byte[] body) {
    String payload = new String(body, StandardCharsets.UTF_8);

    try {
      Map<String, Object> values = jsonMapper.readValue(payload, Map.class);

      return new MailSendMessage(
          asLong(firstNonNull(values.get("emailId"), values.get("mailId"))),
          asLong(values.get("userId")),
          asString(values.get("subject")),
          asString(values.get("content")),
          asString(values.get("receiverEmail")),
          asLocalDateTime(values.get("reserveDatetime")),
          asString(values.get("messageId"))
      );
    } catch (Exception exception) {
      log.error("Failed to parse mail send message payload: {}", payload, exception);
      throw new AmqpRejectAndDontRequeueException("Invalid mail send message payload", exception);
    }
  }

  private Object firstNonNull(Object first, Object second) {
    return first != null ? first : second;
  }

  private Long asLong(Object value) {
    if (value == null) {
      return null;
    }

    if (value instanceof Number number) {
      return number.longValue();
    }

    return Long.valueOf(value.toString());
  }

  private String asString(Object value) {
    return value == null ? null : value.toString();
  }

  private LocalDateTime asLocalDateTime(Object value) {
    if (value == null) {
      return null;
    }

    return LocalDateTime.parse(value.toString());
  }
}
