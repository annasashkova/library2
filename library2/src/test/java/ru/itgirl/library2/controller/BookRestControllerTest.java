package ru.itgirl.library2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library2.dto.BookCreateDTO;
import ru.itgirl.library2.dto.BookDTO;
import ru.itgirl.library2.dto.BookUpdateDTO;
import ru.itgirl.library2.service.BookServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookServiceImpl bookService;

    @Test
    public void testGetBookByName() throws Exception {
        String bookName = "Мастер и Маргарита";
        BookDTO expectedBook = new BookDTO();
        expectedBook.setId(1L);
        expectedBook.setName(bookName);
        when(bookService.getByNameV1(bookName)).thenReturn(expectedBook);
        mockMvc.perform(MockMvcRequestBuilders.get("/book")
                        .param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedBook.getName()));
        verify(bookService).getByNameV1(bookName);
    }

    @Test
    public void testCreateBook() throws Exception {
        String jsonContent = "{"
                + "\"name\": \"Книга\","
                + "\"genreId\": 5"
                + "}";
        BookDTO expectedBook = new BookDTO();
        expectedBook.setId(10L);
        expectedBook.setName("Книга");
        when(bookService.createBook(any(BookCreateDTO.class))).thenReturn(expectedBook);
        mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedBook.getName()));
        verify(bookService).createBook(any(BookCreateDTO.class));
    }

    @Test
    public void testUpdateBook() throws Exception {
        String jsonContent = "{"
                + "\"id\": 10,"
                + "\"name\": \"Книга\","
                + "\"genreId\": 1"
                + "}";
        BookDTO expectedBook = new BookDTO();
        expectedBook.setId(10L);
        expectedBook.setName("Книга");
        when(bookService.updateBook(any(BookUpdateDTO.class))).thenReturn(expectedBook);
        mockMvc.perform(MockMvcRequestBuilders.put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedBook.getName()));
        verify(bookService).updateBook(any(BookUpdateDTO.class));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 5L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/{id}", bookId))
                .andExpect(status().isOk());
        verify(bookService).deleteBook(bookId);
    }
}
