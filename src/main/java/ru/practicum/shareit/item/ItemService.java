package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ItemDto createItem(Long userId, Item item) {
        log.info("Пришел запрос на создание вещи с названием = {}", item.getName());
        checkNewItem(item);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        Item saveItem = itemRepository.save(item);
        return ItemMapper.toItemDto(saveItem, commentRepository.findByItemId(saveItem.getId()));
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAll(Long userId) {
        log.info("Пришел запрос на вывод информации о всех вещах владельца id = {}", userId);
        return itemRepository.findByOwnerId(userId)
                .stream()
                .map((Item x) -> {
                    ItemDto itemDto = ItemMapper.toItemDto(x,
                            commentRepository.findByItemId(x.getId())
                            );
                    return itemDto;
                })
                .toList();
    }

    @Transactional
    public ItemDto updateItem(Long id, Item item, Long userId) {
        log.info("Пришел запрос на обновление вещи с id = {}", id);
        Item oldItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Beщь не найдена"));
        if (!oldItem.getOwner().getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Владелец вещи не совпадает");
        if (item.getName() == null)
            item.setName(oldItem.getName());
        if (item.getDescription() == null)
            item.setDescription(oldItem.getDescription());
        if (item.getAvailable() == null)
            item.setAvailable(oldItem.getAvailable());
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        item.setId(id);
        return ItemMapper.toItemDto(itemRepository.save(item),commentRepository.findByItemId(id));
    }

    @Transactional(readOnly = true)
    public ItemDto findById(Long id) {
        log.info("Пришел запрос на поиск вещи с id = {}", id);
        return ItemMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Вещь не найдена")),
                commentRepository.findByItemId(id));
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findOfText(String text) {
        log.info("Пришел запрос на поиск вещи с описанием = {}", text);
        if (text.equals(""))
            return List.of();
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text,text).stream()
                .map((Item x) -> {
                    ItemDto itemDto = ItemMapper.toItemDto(x,commentRepository.findByItemId(x.getId()));
                    return itemDto;
                })
                .filter(x -> x.getAvailable().booleanValue() == true)
                .toList();
    }

    @Transactional
    public CommentDto createComment(Long itemId, Long userId, Comment comment) {
        log.info("Пришел запрос на создание комментария");
        LocalDateTime dateTimeComment = LocalDateTime.now();
        bookingRepository.findByItemAndBooker(itemId,userId, dateTimeComment)
                .orElseThrow(() -> new ValidationException("Бронирование не доступны"));
        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Вещь не найдена")));
        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        return CommentMapper.toCommentDto(commentRepository.save(comment), dateTimeComment);
    }

    private boolean checkNewItem(Item newItem) {
        if (newItem.getName() == null || newItem.getName().isEmpty())
            throw new ValidationException("Название не задано");
        if (newItem.getDescription() == null || newItem.getDescription().isEmpty())
            throw new ValidationException("Описание не задано");
        if (newItem.getAvailable() == null)
            throw new ValidationException("Статус доступности не задан");
        return true;
    }
}
