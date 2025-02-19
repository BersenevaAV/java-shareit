package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item, List<Comment> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null,
                item.getOwner(),
                comments.stream()
                        .map(CommentMapper::toCommentDto)
                        .toList(),
                null,
                null
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null,
                item.getOwner(),
                null,
                null,
                null
        );
    }

    public static Item toItem(ItemRequestDto itemRequestDto, User owner, ItemRequest request) {
        return new Item(
                itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.isAvailable(),
                owner,
                request
        );
    }
}
