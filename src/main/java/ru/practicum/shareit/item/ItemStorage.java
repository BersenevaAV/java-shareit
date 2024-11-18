package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

@Component
public class ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long idGenerator = 1L;

    public Item createItem(Long userId, Item item) {
        item.setOwner(new User());
        checkNewItem(item);
        item.setId(idGenerator++);
        items.put(item.getId(),item);
        return item;
    }

    public List<Item> getAll(Long userId) {
        return items.values().stream()
                .filter(x -> x.getOwner().equals(userId))
                .toList();
    }

    public Item updateItem(Long id, Item item, Long userId) {
        if (!items.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Объект не найден");
        Item updateItem = items.get(id);
        if (!updateItem.getOwner().equals(userId))
            throw new ValidationException("Владелец вещи не совпадает");
        if (item.getName() != null)
            updateItem.setName(item.getName());
        if (item.getDescription() != null)
            updateItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            updateItem.setAvailable(item.getAvailable());
        return updateItem;
    }

    public Optional<Item> findById(Long id) {
        return items.values().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    public List<Item> findOfText(String text) {
        if (text.equals("")) {
            return items.values().stream()
                    .filter(x -> (x.getAvailable().booleanValue() == true &&
                            (x.getName().equals("") || x.getDescription().equals(""))))
                    .toList();
        }
        return items.values().stream()
                .filter(x -> (x.getAvailable().booleanValue() == true &&
                        (x.getName().toLowerCase().indexOf(text.toLowerCase()) >= 0
                                || x.getDescription().toLowerCase().indexOf(text.toLowerCase()) >= 0)))
                .toList();
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