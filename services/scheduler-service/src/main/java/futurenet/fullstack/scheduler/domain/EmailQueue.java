package futurenet.fullstack.scheduler.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailQueue {

  private Long queueId;
  private Long emailId;
  private String exchangeName;
  private String routingKey;
  private String messageId;
  private String payload;
  private String status;
}
