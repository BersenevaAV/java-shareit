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
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
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
    void createUserWithDuplicateEmail() {
        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("us@mail.ru");
        userService.createUser(user);
        assertThrows(DataIntegrityViolationException.class,() -> userService.createUser(user2));
    }

    @Test
    void createUserWithEmptyName() {
        User user2 = new User();
        user2.setName(null);
        user2.setEmail("us2@mail.ru");
        userService.createUser(user);
        assertThrows(DataIntegrityViolationException.class,() -> userService.createUser(user2));
    }

    @Test
    void updateRightUser() {
        UserDto newUser = userService.createUser(user);
        UserDto updatedUserWithoutName = userService.updateUser(newUser.getId(), new User(null,"descUpdated"));
        UserDto updatedUserWithoutEmail = userService.updateUser(newUser.getId(), new User("userUpdated", null));
        assertEquals("user", updatedUserWithoutName.getName());
        assertEquals("userUpdated", updatedUserWithoutEmail.getName());
        assertEquals("descUpdated", updatedUserWithoutEmail.getEmail());
    }
}