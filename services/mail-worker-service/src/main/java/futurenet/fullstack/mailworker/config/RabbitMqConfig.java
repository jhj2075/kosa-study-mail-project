package futurenet.fullstack.mailworker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@EnableRabbit
@Configuration
@EnableConfigurationProperties({
    MailQueueProperties.class,
    MailSenderProperties.class
})
public class RabbitMqConfig {

  @Bean
  public DirectExchange mailExchange(MailQueueProperties properties) {
    return new DirectExchange(properties.exchange(), true, false);
  }

  @Bean
  public Queue mailQueue(MailQueueProperties properties) {
    return new Queue(properties.queue(), true);
  }

  @Bean
  public Binding mailQueueBinding(
      Queue mailQueue,
      DirectExchange mailExchange,
      MailQueueProperties properties
  ) {
    return BindingBuilder.bind(mailQueue)
        .to(mailExchange)
        .with(properties.routingKey());
  }

  @Bean
  public JsonMapper jsonMapper() {
    return new JsonMapper();
  }
}
