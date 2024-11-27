package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    private Item item;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        item = new Item(1L,"item","desc",true,null,null);
        itemRequestDto = new ItemRequestDto("itemReqDto","descReq",true,null);
    }

    @Test
    void checkNewItem() {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        assertEquals(item.getName(),itemDto.getName());
    }

    @Test
    void checkNewItemWithComments() {
        Comment comment = new Comment(1L, "ok", item, new User("user", "us@mail.ru"));
        ItemDto itemDto = ItemMapper.toItemDto(item, List.of(comment));
        assertTrue(itemDto.getComments().stream()
                .filter((x) -> x.getText().equals("ok"))
                .findFirst().isPresent());
    }

    @Test
    void checkNewItemDto() {
        Item newItem = ItemMapper.toItem(itemRequestDto, null,null);
        assertEquals(newItem.getName(),itemRequestDto.getName());
    }
}