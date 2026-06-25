package futurenet.fullstack.reservation.support;

import futurenet.fullstack.reservation.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class ReservationUserResolver {

  public Long currentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
      throw new AuthenticationCredentialsNotFoundException("인증된 사용자 정보를 찾을 수 없습니다.");
    }

    return user.userId();
  }
}
