package futurenet.fullstack.mailworker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail-worker")
public record MailSenderProperties(
    String from,
    Integer maxRetryCount
) {

  public int retryLimit() {
    if (maxRetryCount == null || maxRetryCount < 0) {
      return 3;
    }

    return maxRetryCount;
  }
}
