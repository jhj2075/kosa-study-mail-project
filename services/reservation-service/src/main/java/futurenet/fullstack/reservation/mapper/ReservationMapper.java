package futurenet.fullstack.reservation.mapper;

import futurenet.fullstack.reservation.domain.EmailReservation;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

public interface ReservationMapper {

  int insert(EmailReservation reservation);

  Optional<EmailReservation> findByIdAndUserId(
      @Param("emailId") Long emailId,
      @Param("userId") Long userId
  );

  List<EmailReservation> findAllByUserId(
      @Param("userId") Long userId
  );

  int updateWaitingReservation(EmailReservation reservation);

  int cancelWaitingReservation(
      @Param("emailId") Long emailId,
      @Param("userId") Long userId
  );

  int deleteCanceledReservation(
      @Param("emailId") Long emailId,
      @Param("userId") Long userId
  );
}