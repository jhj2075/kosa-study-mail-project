package futurenet.fullstack.reservation.domain;

public enum ReservationStatus {
  WAITING,
  PROCESSING,
  QUEUED,
  SENDING,
  SENT,
  FAILED,
  CANCELED
}
