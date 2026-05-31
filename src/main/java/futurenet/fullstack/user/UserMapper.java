package futurenet.fullstack.user;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  List<User> findAll();
}
