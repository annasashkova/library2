package ru.itgirl.library2.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library2.dto.AuthorCreateDTO;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.AuthorUpdateDTO;
import ru.itgirl.library2.model.Author;
import ru.itgirl.library2.model.Book;
import ru.itgirl.library2.repository.AuthorRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        AuthorDTO authorDto = authorService.getAuthorById(id);
        verify(authorRepository).findById(id);
        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getName(), author.getName());
        assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetAuthorByName() {
        Long id = 2L;
        String name = "John";
        String surname = "Martin";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.of(author));
        AuthorDTO authorDto = authorService.getAuthorByName(name);
        verify(authorRepository).findAuthorByName(name);
        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getName(), author.getName());
        assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameNotFound() {
        String name = "John";
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByName(name));
        verify(authorRepository).findAuthorByName(name);
    }

    @Test
    public void testCreateAuthor() {
        String name = "John";
        String surname = "Martin";
        AuthorCreateDTO createDto = new AuthorCreateDTO(name, surname);
        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName(createDto.getName());
        savedAuthor.setSurname(createDto.getSurname());
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        AuthorDTO result = authorService.createAuthor(createDto);
        verify(authorRepository).save(any(Author.class));
        assertEquals(savedAuthor.getId(), result.getId());
        assertEquals(savedAuthor.getName(), result.getName());
        assertEquals(savedAuthor.getSurname(), result.getSurname());
    }

    @Test
    public void testUpdateAuthorSuccess() {
        AuthorUpdateDTO updateDto = new AuthorUpdateDTO();
        updateDto.setId(1L);
        updateDto.setName("Лев");
        updateDto.setSurname("Толстой");
        Author updatedAuthor = new Author();
        updatedAuthor.setId(1L);
        updatedAuthor.setName("Старое имя");
        updatedAuthor.setSurname("Старая фамилия");
        when(authorRepository.findById(updateDto.getId())).thenReturn(Optional.of(updatedAuthor));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AuthorDTO result = authorService.updateAuthor(updateDto);
        verify(authorRepository).findById(updateDto.getId());
        verify(authorRepository).save(any(Author.class));
        assertEquals(updateDto.getId(), result.getId());
        assertEquals(updateDto.getName(), result.getName());
        assertEquals(updateDto.getSurname(), result.getSurname());
    }

    @Test
    public void testUpdateAuthorNotFound() {
        AuthorUpdateDTO updateDto = new AuthorUpdateDTO();
        updateDto.setId(999L);
        updateDto.setName("Имя");
        updateDto.setSurname("Фамилия");
        when(authorRepository.findById(updateDto.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            authorService.updateAuthor(updateDto);
        });
        verify(authorRepository).findById(updateDto.getId());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {
        Long authorId = 1L;
        authorService.deleteAuthor(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    public void testGetAllAuthors() {
        Author author1 = new Author(
                1L,
                "Имя 1",
                "Фамилия 1",
                new HashSet<>()
        );
        Author author2 = new Author(
                2L,
                "Имя 2",
                "Фамилия 2",
                new HashSet<>()
        );
        List<Author> authors = Arrays.asList(author1, author2);
        when(authorRepository.findAll()).thenReturn(authors);
        AuthorServiceImpl spyService = spy(authorService);
        doAnswer(invocation -> {
            Author auth = invocation.getArgument(0);
            return new AuthorDTO(
                    auth.getId(),
                    auth.getName(),
                    auth.getSurname(),
                    Collections.emptyList()
            );
        }).when(spyService).convertEntityToDto(any(Author.class));
        List<AuthorDTO> result = spyService.getAllAuthors();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Имя 1", result.get(0).getName());
        assertEquals("Фамилия 1", result.get(0).getSurname());
        assertTrue(result.get(0).getBooks().isEmpty());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Имя 2", result.get(1).getName());
        assertEquals("Фамилия 2", result.get(1).getSurname());
        assertTrue(result.get(1).getBooks().isEmpty());
        verify(authorRepository).findAll();
        verify(spyService, times(2)).convertEntityToDto(any(Author.class));
    }
}
