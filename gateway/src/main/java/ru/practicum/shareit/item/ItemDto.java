package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank(message = "Name should be not null")
    private String name;
    @NotBlank(message = "Description should be not null")
    private String description;
    @NotNull(message = "Available should be not null")
    private Boolean available;
    private Long requestId;
}
