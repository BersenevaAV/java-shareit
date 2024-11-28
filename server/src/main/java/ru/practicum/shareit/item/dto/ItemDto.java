package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private User owner;
    private List<CommentDto> comments;
    private Item lastBooking;
    private Item nextBooking;
}
