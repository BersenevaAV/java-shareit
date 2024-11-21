package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto createUser(User user) {
        log.info("Пришел запрос на создание пользователя с name = {}",user.getName());
        if (!user.getEmail().contains("@"))
            throw new ValidationException("Неправильно задан email");
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        log.info("Пришел запрос на вывод информации о всех пользователях");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        log.info("Пришел запрос на поиск пользователя с id = {}", id);
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
    }

    @Transactional
    public UserDto updateUser(Long id, User user) {
        log.info("Пришел запрос на обновление пользователя с id = {}", id);
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        user.setId(id);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Пришел запрос на удаление пользователя с id = {}", id);
        userRepository.deleteById(id);
    }
}

