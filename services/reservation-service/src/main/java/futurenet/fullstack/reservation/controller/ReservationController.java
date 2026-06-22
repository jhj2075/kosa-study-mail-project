package futurenet.fullstack.reservation.controller;

import futurenet.fullstack.reservation.dto.ReservationCreateRequest;
import futurenet.fullstack.reservation.dto.ReservationResponse;
import futurenet.fullstack.reservation.dto.ReservationUpdateRequest;
import futurenet.fullstack.reservation.service.ReservationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping
  public ResponseEntity<ReservationResponse> create(
      @Valid @RequestBody ReservationCreateRequest request
  ) {
    ReservationResponse response =
        reservationService.create(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @GetMapping("/{emailId}")
  public ResponseEntity<ReservationResponse> findById(
      @PathVariable Long emailId,
      @RequestParam Long userId
  ) {
    ReservationResponse response =
        reservationService.findById(emailId, userId);

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<ReservationResponse>> findAll(
      @RequestParam Long userId
  ) {
    List<ReservationResponse> response =
        reservationService.findAllByUserId(userId);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{emailId}")
  public ResponseEntity<ReservationResponse> update(
      @PathVariable Long emailId,
      @RequestParam Long userId,
      @Valid @RequestBody ReservationUpdateRequest request
  ) {
    ReservationResponse response =
        reservationService.update(emailId, userId, request);

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{emailId}/cancel")
  public ResponseEntity<Void> cancel(
      @PathVariable Long emailId,
      @RequestParam Long userId
  ) {
    reservationService.cancel(emailId, userId);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{emailId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long emailId,
      @RequestParam Long userId
  ) {
    reservationService.delete(emailId, userId);

    return ResponseEntity.noContent().build();
  }
}