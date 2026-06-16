package futurenet.fullstack.reservation.service;

import futurenet.fullstack.reservation.domain.EmailReservation;
import futurenet.fullstack.reservation.domain.ReservationStatus;
import futurenet.fullstack.reservation.dto.ReservationCreateRequest;
import futurenet.fullstack.reservation.dto.ReservationResponse;
import futurenet.fullstack.reservation.dto.ReservationUpdateRequest;
import futurenet.fullstack.reservation.exception.ReservationNotFoundException;
import futurenet.fullstack.reservation.mapper.ReservationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

  private final ReservationMapper reservationMapper;

  @Transactional
  public ReservationResponse create(ReservationCreateRequest request) {
    EmailReservation reservation = EmailReservation.builder()
        .userId(request.userId())
        .subject(request.subject())
        .content(request.content())
        .receiverEmail(request.receiverEmail())
        .reserveDatetime(request.reserveDatetime())
        .status(ReservationStatus.WAITING)
        .build();

    int insertedCount = reservationMapper.insert(reservation);

    if (insertedCount != 1) {
      throw new IllegalStateException("예약 메일 등록에 실패했습니다.");
    }

    return reservationMapper
        .findByIdAndUserId(reservation.getEmailId(), request.userId())
        .map(ReservationResponse::from)
        .orElseThrow(() ->
            new ReservationNotFoundException(reservation.getEmailId()));
  }

  public ReservationResponse findById(Long emailId, Long userId) {
    return reservationMapper
        .findByIdAndUserId(emailId, userId)
        .map(ReservationResponse::from)
        .orElseThrow(() -> new ReservationNotFoundException(emailId));
  }

  public List<ReservationResponse> findAllByUserId(Long userId) {
    return reservationMapper.findAllByUserId(userId)
        .stream()
        .map(ReservationResponse::from)
        .toList();
  }

  @Transactional
  public ReservationResponse update(
      Long emailId,
      Long userId,
      ReservationUpdateRequest request
  ) {
    EmailReservation reservation = EmailReservation.builder()
        .emailId(emailId)
        .userId(userId)
        .subject(request.subject())
        .content(request.content())
        .receiverEmail(request.receiverEmail())
        .reserveDatetime(request.reserveDatetime())
        .build();

    int updatedCount =
        reservationMapper.updateWaitingReservation(reservation);

    if (updatedCount != 1) {
      throw new IllegalStateException(
          "존재하지 않거나 수정할 수 없는 예약 메일입니다."
      );
    }

    return findById(emailId, userId);
  }

  @Transactional
  public void cancel(Long emailId, Long userId) {
    int updatedCount =
        reservationMapper.cancelWaitingReservation(emailId, userId);

    if (updatedCount != 1) {
      throw new IllegalStateException(
          "존재하지 않거나 취소할 수 없는 예약 메일입니다."
      );
    }
  }

  @Transactional
  public void delete(Long emailId, Long userId) {
    int deletedCount =
        reservationMapper.deleteCanceledReservation(emailId, userId);

    if (deletedCount != 1) {
      throw new IllegalStateException(
          "존재하지 않거나 삭제할 수 없는 예약 메일입니다."
      );
    }
  }
}