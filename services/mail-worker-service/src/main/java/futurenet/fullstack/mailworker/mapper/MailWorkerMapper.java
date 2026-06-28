package futurenet.fullstack.mailworker.mapper;

import futurenet.fullstack.mailworker.domain.EmailReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MailWorkerMapper {

  EmailReservation findReservationByEmailId(@Param("emailId") Long emailId);

  Long findQueueIdByMessageId(@Param("messageId") String messageId);

  int markSending(@Param("emailId") Long emailId);

  int markSent(@Param("emailId") Long emailId);

  int markFailed(@Param("emailId") Long emailId);

  int markQueueConsumed(@Param("queueId") Long queueId);

  int markQueueFailed(@Param("queueId") Long queueId);

  int insertSendLog(
      @Param("emailId") Long emailId,
      @Param("queueId") Long queueId,
      @Param("status") String status,
      @Param("message") String message
  );
}
