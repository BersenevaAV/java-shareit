package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.ru");
        User saveUser = userRepository.save(user);
        ItemRequest request = new ItemRequest();
        request.setUser(saveUser);
        request.setCreated(LocalDateTime.now());
        request.setDescription("desc");
        ItemRequest saveRequest = requestRepository.save(request);
        List<ItemRequest> requests = requestRepository.findByUserId(saveUser.getId());
        assertEquals(1, requests.size());
        assertTrue(requests.contains(saveRequest));
    }
}