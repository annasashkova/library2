package ru.itgirl.library2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library2.dto.AuthorCreateDTO;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.AuthorUpdateDTO;
import ru.itgirl.library2.service.AuthorService;
import ru.itgirl.library2.service.AuthorServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");
        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        String authorName = "Александр";
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setId(1L);
        authorDto.setName(authorName);
        authorDto.setSurname("Пушкин");
        when(authorService.getAuthorByName(authorName)).thenReturn(authorDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/author")
                        .param("name", authorName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
        verify(authorService).getAuthorByName(authorName);
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorCreateDTO createDto = new AuthorCreateDTO();
        createDto.setName("Лев");
        createDto.setSurname("Толстой");
        AuthorDTO createdDto = new AuthorDTO();
        createdDto.setId(2L);
        createdDto.setName(createDto.getName());
        createdDto.setSurname(createDto.getSurname());
        when(authorService.createAuthor(any(AuthorCreateDTO.class))).thenReturn(createdDto);
        String requestBody = new ObjectMapper().writeValueAsString(createDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(createdDto.getSurname()));
        verify(authorService).createAuthor(any(AuthorCreateDTO.class));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorUpdateDTO updateDto = new AuthorUpdateDTO();
        updateDto.setId(1L);
        updateDto.setName("Александр");
        updateDto.setSurname("Пушкин");
        AuthorDTO updatedDto = new AuthorDTO();
        updatedDto.setId(updateDto.getId());
        updatedDto.setName(updateDto.getName());
        updatedDto.setSurname(updateDto.getSurname());
        when(authorService.updateAuthor(any(AuthorUpdateDTO.class))).thenReturn(updatedDto);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(updatedDto.getSurname()));
        verify(authorService).updateAuthor(any(AuthorUpdateDTO.class));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long authorId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/{id}", authorId))
                .andExpect(status().isOk());
        verify(authorService).deleteAuthor(authorId);
    }
}
