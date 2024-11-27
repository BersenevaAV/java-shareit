package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
class ItemRequestServiceSpringBootTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserService userService;
    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("user");
        user.setEmail("us@mail.ru");
        request = new ItemRequest();
        request.setDescription("request");
    }

    @Test
    void getAllRequest() {
        UserDto newUser = userService.createUser(user);
        ItemRequestDto newRequest = itemRequestService.createRequest(newUser.getId(),request);
        List<ItemRequest> requests = itemRequestService.getAll(newUser.getId());
        assertEquals(1, requests.size());
        assertTrue(requests.stream()
                .filter((x) -> (x.getDescription().equals("request")))
                .findFirst().isPresent());
    }
}