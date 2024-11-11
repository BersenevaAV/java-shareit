package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ValidationException;
import java.util.*;

@Component
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long idGenerator = 1L;

    public User createUser(User user) {
        checkNewUser(user);
        user.setId(idGenerator++);
        users.put(user.getId(),user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        if (id == null)
            throw new ValidationException("Не задан id пользователя");
        Optional<User> user = users.values().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        return user;
    }

    public User updateUser(Long id, User user) {
        if (users.containsKey(id)) {
            User updateUser = users.get(id);
            if (user.getEmail() != null) {
                checkEmail(user);
                updateUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                updateUser.setName(user.getName());
            }
            return updateUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден");
        }
    }

    private void checkNewUser(User newUser) {
        if (!newUser.getEmail().isEmpty())
            checkEmail(newUser);
        else
            throw new ValidationException("Email пустой");
    }

    public void deleteUser(Long id) {
        if (!users.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неизвестный id");
        users.remove(id);
    }

    private void checkEmail(User newUser) {
        boolean isNotSameEmail = users.values().stream()
                .filter(x -> x.getEmail().equals(newUser.getEmail()))
                .findFirst()
                .isEmpty();
        boolean isCorrectEmail = newUser.getEmail().contains("@");
        if (!isCorrectEmail)
            throw new ValidationException("Email задан неверно");
        if (!isNotSameEmail)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email совпадает");
    }
}
