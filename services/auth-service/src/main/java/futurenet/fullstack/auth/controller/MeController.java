package futurenet.fullstack.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/auth/me")
    public String me(Authentication authentication) {
        return authentication.getName();
    }
}