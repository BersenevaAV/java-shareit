package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserDto createUser(User user) {
        log.info("Пришел запрос на создание пользователя с name = {}",user.getName());
        return UserMapper.toUserDto(userStorage.createUser(user));
    }

    public List<UserDto> getAll() {
        log.info("Пришел запрос на вывод информации о всех пользователях");
        return userStorage.getAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        log.info("Пришел запрос на поиск пользователя с id = {}", id);
        return UserMapper.toUserDto(userStorage.findById(id).orElse(new User()));
    }

    public UserDto updateUser(Long id, User user) {
        log.info("Пришел запрос на обновление пользователя с id = {}", id);
        return UserMapper.toUserDto(userStorage.updateUser(id, user));
    }

    public void deleteUser(Long id) {
        log.info("Пришел запрос на удаление пользователя с id = {}", id);
        userStorage.deleteUser(id);
    }
}

