package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto createItem(Long userId, Item item) {
        log.info("Пришел запрос на создание вещи с названием = {}", item.getName());
        userStorage.findById(userId);
        return ItemMapper.toItemDto(itemStorage.createItem(userId, item));
    }

    public List<ItemDto> getAll(Long userId) {
        log.info("Пришел запрос на вывод информации о всех вещах владельца id = {}", userId);
        return itemStorage.getAll(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public ItemDto updateItem(Long id, Item item, Long userId) {
        log.info("Пришел запрос на обновление вещи с id = {}", id);
        userStorage.findById(userId);
        return ItemMapper.toItemDto(itemStorage.updateItem(id, item, userId));
    }

    public ItemDto findById(Long id) {
        log.info("Пришел запрос на поиск вещи с id = {}", id);
        return ItemMapper.toItemDto(itemStorage.findById(id)
                .orElseThrow(() -> new ValidationException("Вещь не найдена")));
    }

    public List<ItemDto> findOfText(String text) {
        log.info("Пришел запрос на поиск вещи с описанием = {}", text);
        return itemStorage.findOfText(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
