package futurenet.fullstack.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {

  public ReservationNotFoundException(Long emailId) {
    super("예약 메일을 찾을 수 없습니다. emailId=" + emailId);
  }
}