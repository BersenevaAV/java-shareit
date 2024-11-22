package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByOwnerId() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        User user1 = new User("User1", "email1");
        User saveUser1 = userRepository.save(user1);
        item.setOwner(saveUser1);
        Item saveItem = itemRepository.save(item);
        itemRepository.findByOwnerId(saveItem.getId());
        assertFalse(itemRepository.findByOwnerId(saveUser1.getId()).isEmpty(), "Не найдены вещи владельца");
        User user2 = new User("User2", "email2");
        User saveUser2 = userRepository.save(user2);
        assertTrue(itemRepository.findByOwnerId(saveUser2.getId()).isEmpty(), "Найдены вещи владельца");
    }

    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        itemRepository.save(item);
        assertFalse(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("1","1").isEmpty(),
                "Не найдены вещи по запросу");
    }
}