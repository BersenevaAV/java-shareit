package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Пришел запрос на создание запроса {}", requestDto.getDescription());
        return itemRequestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findByIdUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пришел запрос на поиск запроса пользователя с id = {}", userId);
        return itemRequestClient.findByIdUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAvailableRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пришел запрос на поиск доступных запросов пользователю с id = {}", userId);
        return itemRequestClient.findAvailableRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable long requestId) {
        log.info("Пришел запрос на поиск запроса c id = {}", requestId);
        return itemRequestClient.findById(userId, requestId);
    }
}
