package futurenet.fullstack.mailworker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail-queue")
public record MailQueueProperties(
    String exchange,
    String routingKey,
    String queue,
    String deadLetterExchange,
    String deadLetterQueue
) {
}
