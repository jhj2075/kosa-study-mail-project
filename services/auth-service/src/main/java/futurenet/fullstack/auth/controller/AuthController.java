package futurenet.fullstack.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import futurenet.fullstack.auth.dto.LoginRequest;
import futurenet.fullstack.auth.dto.LoginResponse;
import futurenet.fullstack.auth.dto.RegisterRequest;
import futurenet.fullstack.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/auth/register")
    public String register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return "registered";
    }
}
