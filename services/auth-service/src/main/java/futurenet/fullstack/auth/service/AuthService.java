package futurenet.fullstack.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import futurenet.fullstack.auth.dto.LoginRequest;
import futurenet.fullstack.auth.dto.LoginResponse;
import futurenet.fullstack.auth.dto.RegisterRequest;
import futurenet.fullstack.auth.entity.User;
import futurenet.fullstack.auth.jwt.JwtUtil;
import futurenet.fullstack.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByLoginId(request.getLoginId());

        if (user == null) {
            throw new RuntimeException("Invalid login id or password.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid login id or password.");
        }

        if (!ACTIVE_STATUS.equals(user.getStatus())) {
            throw new RuntimeException("Inactive user account.");
        }

        String accessToken = jwtUtil.generateToken(user);

        return new LoginResponse(accessToken, "Bearer");
    }

    public void register(RegisterRequest request) {
        User existingLoginId = userMapper.findByLoginId(request.getLoginId());

        if (existingLoginId != null) {
            throw new RuntimeException("Login id already exists.");
        }

        User existingEmail = userMapper.findByEmail(request.getEmail());

        if (existingEmail != null) {
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();
        user.setLoginId(request.getLoginId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setStatus(ACTIVE_STATUS);

        userMapper.insertUser(user);
    }
}
