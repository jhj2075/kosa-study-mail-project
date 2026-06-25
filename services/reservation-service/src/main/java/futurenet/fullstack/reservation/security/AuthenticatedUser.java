package futurenet.fullstack.reservation.security;

public record AuthenticatedUser(
    Long userId,
    String loginId
) {

}
