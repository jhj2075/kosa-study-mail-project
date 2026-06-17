package futurenet.fullstack.reservation.support;

import org.springframework.stereotype.Component;

@Component
public class ReservationUserResolver {

  private static final Long TEMPORARY_USER_ID = 1L;

  public Long currentUserId() {
    return TEMPORARY_USER_ID;
  }
}
