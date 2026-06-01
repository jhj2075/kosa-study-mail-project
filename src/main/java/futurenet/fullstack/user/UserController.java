package futurenet.fullstack.user;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserMapper userMapper;

  public UserController(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @GetMapping("/users")
  public List<User> users() {
    return userMapper.findAll();
  }
}
