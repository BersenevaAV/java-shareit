package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequest request) {
        log.info("Пришел запрос на создание запроса {}", request.getDescription());
        request.setCreated(LocalDateTime.now());
        request.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(request), null);
    }

    @Transactional
    public List<ItemRequest> getUserRequest(Long userId) {
        log.info("Пришел запрос на поиск запроса пользователя с id = {}", userId);
        return requestRepository.findByUserId(userId);
    }

    @Transactional
    public List<ItemRequest> getAll(Long userId) {
        log.info("Пришел запрос на поиск доступных запросов пользователю с id = {}", userId);
        return requestRepository.findAll();
    }

    @Transactional
    public ItemRequestDto findById(Long  userId, Long requestId) {
        log.info("Пришел запрос на поиск запроса c id = {}", requestId);
        List<ItemDto> items =  itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
        return ItemRequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Запрос не найден")),items);
    }
}