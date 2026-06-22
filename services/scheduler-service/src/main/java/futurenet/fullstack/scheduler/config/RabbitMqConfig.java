package futurenet.fullstack.scheduler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MailQueueProperties.class)
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
  public MessageConverter messageConverter() {
    return new SimpleMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
      MessageConverter messageConverter
  ) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    return rabbitTemplate;
  }
}
