package futurenet.fullstack.auth.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String role;
}