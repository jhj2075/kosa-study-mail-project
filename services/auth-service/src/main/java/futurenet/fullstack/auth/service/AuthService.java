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

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
          throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateToken(user);

        return new LoginResponse(accessToken, "Bearer");
    }
    
    public void register(RegisterRequest request) {
      User existingUser = userMapper.findByEmail(request.getEmail());

      if (existingUser != null) {
          throw new RuntimeException("이미 존재하는 이메일입니다.");
      }

      User user = new User();
      user.setEmail(request.getEmail());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setName(request.getName());
      user.setRole("USER");

      userMapper.insertUser(user);
  }
}