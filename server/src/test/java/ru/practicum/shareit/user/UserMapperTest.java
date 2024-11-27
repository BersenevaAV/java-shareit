package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto() {
        User user = new User("user", "user@mail.ru");
        UserDto userDto = UserMapper.toUserDto(user);
        assertEquals(user.getName(),userDto.getName());
    }
}