package home.product.mapper;

import home.product.dto.UserDto;
import home.product.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getId(), user.getUsername());
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        return user;
    }
}