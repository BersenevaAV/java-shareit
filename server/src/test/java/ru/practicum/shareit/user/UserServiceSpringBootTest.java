package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
class UserServiceSpringBootTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("user");
        user.setEmail("us@mail.ru");
    }

    @Test
    void createRightUser() {
        UserDto newUser = userService.createUser(user);
        assertEquals(newUser.getName(),"user");
        assertEquals(newUser.getEmail(),"us@mail.ru");
    }

    @Test
    void createUserWithDuplicate() {
        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("us@mail.ru");
        userService.createUser(user);
        assertThrows(DataIntegrityViolationException.class,() -> userService.createUser(user2));
    }
}