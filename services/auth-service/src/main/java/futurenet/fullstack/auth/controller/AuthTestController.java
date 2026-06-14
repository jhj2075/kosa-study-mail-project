package futurenet.fullstack.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import futurenet.fullstack.auth.entity.User;
import futurenet.fullstack.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthTestController {

    private final UserMapper userMapper;

    @GetMapping("/user-test")
    public User userTest() {
        return userMapper.findByEmail("test@test.com");
    }
}