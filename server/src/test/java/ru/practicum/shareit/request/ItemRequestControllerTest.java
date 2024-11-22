package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        itemRequestDto = new ItemRequestDto(1L, "", LocalDateTime.now(), null);
    }

    @Test
    void createRequest() throws Exception {
    }
}