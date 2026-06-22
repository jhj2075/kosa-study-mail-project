package futurenet.fullstack.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import futurenet.fullstack.auth.dto.LoginRequest;
import futurenet.fullstack.auth.dto.LoginResponse;
import futurenet.fullstack.auth.dto.RegisterRequest;
import futurenet.fullstack.auth.jwt.JwtAuthenticationFilter;
import futurenet.fullstack.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private static final Duration ACCESS_TOKEN_MAX_AGE = Duration.ofHours(1);

    private final AuthService authService;

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }

        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginRequest") LoginRequest request,
            HttpServletResponse response,
            Model model
    ) {
        try {
            LoginResponse loginResponse = authService.login(request);
            response.addHeader(HttpHeaders.SET_COOKIE, createAccessTokenCookie(loginResponse.getAccessToken()));
            return "redirect:/home";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("registerRequest") RegisterRequest request,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("loginId", authentication.getName());
        return "auth/home";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        response.addHeader(HttpHeaders.SET_COOKIE, expireAccessTokenCookie());
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다.");
        return "redirect:/login";
    }

    private String createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(JwtAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME, accessToken)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .build()
                .toString();
    }

    private String expireAccessTokenCookie() {
        return ResponseCookie.from(JwtAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build()
                .toString();
    }
}
