package futurenet.fullstack.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String loginId;
    private String email;
    private String password;
    private String userName;
}
