package ru.practicum.shareit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemResponceDto {
    private long itemId = 1;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime end = LocalDateTime.now();
}
