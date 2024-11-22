package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemControllerTest {
    @Autowired
    private ItemService itemService;

    @Test
    public void createItem() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("name", "desc", false, 1L);
        assertFalse(itemRequestDto == null);
    }

}