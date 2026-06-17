package futurenet.fullstack.reservation.controller;

import futurenet.fullstack.reservation.dto.ReservationCreateRequest;
import futurenet.fullstack.reservation.dto.ReservationResponse;
import futurenet.fullstack.reservation.dto.ReservationUpdateRequest;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class ReservationForm {

  private Long userId;

  @NotBlank(message = "제목은 필수입니다.")
  @Size(max = 255, message = "제목은 255자 이하여야 합니다.")
  private String subject;

  private String content;

  @NotBlank(message = "받는 사람 이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @Size(max = 255, message = "이메일은 255자 이하여야 합니다.")
  private String receiverEmail;

  @NotNull(message = "예약 시간은 필수입니다.")
  @Future(message = "예약 시간은 현재 시간 이후여야 합니다.")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime reserveDatetime;

  @AssertTrue(message = "예약 시간은 분 단위로만 입력할 수 있습니다.")
  public boolean isReserveDatetimeMinuteUnit() {
    return reserveDatetime == null
        || (reserveDatetime.getSecond() == 0 && reserveDatetime.getNano() == 0);
  }

  public static ReservationForm empty(Long userId) {
    ReservationForm form = new ReservationForm();
    form.setUserId(userId);
    form.setReserveDatetime(LocalDateTime.now().plusHours(1).withSecond(0).withNano(0));
    return form;
  }

  public static ReservationForm from(ReservationResponse response) {
    ReservationForm form = new ReservationForm();
    form.setUserId(response.userId());
    form.setSubject(response.subject());
    form.setContent(response.content());
    form.setReceiverEmail(response.receiverEmail());
    form.setReserveDatetime(response.reserveDatetime());
    return form;
  }

  public ReservationCreateRequest toCreateRequest() {
    return new ReservationCreateRequest(
        userId,
        subject,
        content,
        receiverEmail,
        reserveDatetime
    );
  }

  public ReservationUpdateRequest toUpdateRequest() {
    return new ReservationUpdateRequest(
        subject,
        content,
        receiverEmail,
        reserveDatetime
    );
  }
}
