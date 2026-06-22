package futurenet.fullstack.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail-queue")
public record MailQueueProperties(
    String exchange,
    String routingKey,
    String queue,
    Long schedulerFixedDelayMs
) {
}
