package futurenet.fullstack.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import futurenet.fullstack.auth.entity.User;

@Mapper
public interface UserMapper {

    User findByLoginId(@Param("loginId") String loginId);
    User findByEmail(@Param("email") String email);
    int insertUser(User user);

}

