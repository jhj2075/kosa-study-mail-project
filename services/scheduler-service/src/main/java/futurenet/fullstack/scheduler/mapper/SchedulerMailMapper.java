package futurenet.fullstack.scheduler.mapper;

import futurenet.fullstack.scheduler.domain.EmailQueue;
import futurenet.fullstack.scheduler.domain.EmailReservation;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SchedulerMailMapper {

  List<EmailReservation> findDueWaitingReservations(
      @Param("now") LocalDateTime now
  );

  int markProcessing(
      @Param("emailId") Long emailId
  );

  int markQueued(
      @Param("emailId") Long emailId
  );

  int markFailed(
      @Param("emailId") Long emailId
  );

  int insertQueue(EmailQueue queue);

  int insertSendLog(
      @Param("emailId") Long emailId,
      @Param("queueId") Long queueId,
      @Param("status") String status,
      @Param("message") String message
  );
}
