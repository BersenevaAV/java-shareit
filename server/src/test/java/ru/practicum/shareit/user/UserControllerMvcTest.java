package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerMvcTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetAll() throws Exception {
        when(userService.getAll())
                .thenAnswer(invocationOnMock -> {
                    List<UserDto> users = new ArrayList<>();
                    users.add(new UserDto(1L, "name1", "email1"));
                    users.add(new UserDto(2L, "name2", "email2"));
                    return users;
                });
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].email").value("email1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].email").value("email2"));
    }

    @Test
    public void testGetById() throws Exception {
        long userId = 1;
        UserDto userDto = new UserDto(userId, "User", "email");
        when(userService.findById(userId))
                .thenReturn(userDto);
        mvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("email"));
    }

    @Test
    void createUserThrowDuplicateException() throws Exception {
        User userCreateDto = new User("name", "email");
        when(userService.createUser(userCreateDto))
                .thenThrow(new DataIntegrityViolationException("Данный email уже используется"));
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        long userId = 1L;
        User updateDto = new User("name", "email");
        UserDto expectedResponse = new UserDto(userId, "name", "email");

        when(userService.updateUser(eq(userId),any(User.class)))
                .thenReturn(expectedResponse);

        mvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }

}