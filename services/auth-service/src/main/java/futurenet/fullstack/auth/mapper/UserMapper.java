package futurenet.fullstack.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import futurenet.fullstack.auth.entity.User;

@Mapper
public interface UserMapper {

    User findByEmail(String email);
}