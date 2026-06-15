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
            throw new RuntimeException("???? ?? ??????.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
          throw new RuntimeException("????? ???? ????.");
        }

        String accessToken = jwtUtil.generateToken(user);

        return new LoginResponse(accessToken, "Bearer");
    }
    
    public void register(RegisterRequest request) {
      User existingUser = userMapper.findByEmail(request.getEmail());

      if (existingUser != null) {
          throw new RuntimeException("?? ???? ??????.");
      }

      User user = new User();
      user.setEmail(request.getEmail());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setName(request.getName());
      user.setRole("USER");

      userMapper.insertUser(user);
  }
}