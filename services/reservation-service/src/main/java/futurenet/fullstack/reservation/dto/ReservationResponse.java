package futurenet.fullstack.reservation.dto;

import futurenet.fullstack.reservation.domain.EmailReservation;
import futurenet.fullstack.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponse(
    Long emailId,
    Long userId,
    String subject,
    String content,
    String receiverEmail,
    LocalDateTime reserveDatetime,
    LocalDateTime sentDatetime,
    ReservationStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public static ReservationResponse from(EmailReservation reservation) {
    return new ReservationResponse(
        reservation.getEmailId(),
        reservation.getUserId(),
        reservation.getSubject(),
        reservation.getContent(),
        reservation.getReceiverEmail(),
        reservation.getReserveDatetime(),
        reservation.getSentDatetime(),
        reservation.getStatus(),
        reservation.getCreatedAt(),
        reservation.getUpdatedAt()
    );
  }
}