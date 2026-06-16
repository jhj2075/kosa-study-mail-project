package futurenet.fullstack.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ReservationUpdateRequest(

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자 이하여야 합니다.")
    String subject,

    String content,

    @NotBlank(message = "수신자 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다.")
    String receiverEmail,

    @NotNull(message = "예약 시간은 필수입니다.")
    @Future(message = "예약 시간은 현재 시간 이후여야 합니다.")
    LocalDateTime reserveDatetime
) {

}