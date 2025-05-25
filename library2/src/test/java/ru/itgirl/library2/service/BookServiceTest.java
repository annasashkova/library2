package ru.itgirl.library2.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.BookCreateDTO;
import ru.itgirl.library2.dto.BookDTO;
import ru.itgirl.library2.dto.BookUpdateDTO;
import ru.itgirl.library2.model.Author;
import ru.itgirl.library2.model.Book;
import ru.itgirl.library2.model.Genre;
import ru.itgirl.library2.repository.BookRepository;
import ru.itgirl.library2.repository.GenreRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testGetByNameBookFound() {
        String bookName = "Test Book";
        Genre genre = new Genre(1L, "Genre Name", new HashSet<>());
        Author author = new Author(1L, "Author Name", "Author Surname", new HashSet<>());
        Book book = Book.builder()
                .id(1L)
                .name(bookName)
                .genre(genre)
                .authors(new HashSet<>(Arrays.asList(author)))
                .build();
        when(bookRepository.findBookByName(bookName)).thenReturn(Optional.of(book));
        BookDTO expectedDto = new BookDTO(
                1L,
                bookName,
                genre.getName(),
                Arrays.asList(new AuthorDTO(author.getId(), author.getName(), author.getSurname(), new ArrayList<>()))
        );
        BookServiceImpl spyService = spy(bookService);
        doReturn(expectedDto).when(spyService).convertEntityToDto(any(Book.class));
        BookDTO result = spyService.getByNameV1(bookName);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getGenre(), result.getGenre());
        assertEquals(1, result.getAuthors().size());
        assertEquals("Author Name", result.getAuthors().get(0).getName());
        verify(bookRepository).findBookByName(bookName);
    }


    @Test
    public void testGetByNameBookNotFound() {
        String bookName = "Книга";
        when(bookRepository.findBookByName(bookName)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            bookService.getByNameV1(bookName);
        });
        verify(bookRepository).findBookByName(bookName);
    }

    @Test
    public void testCreateBook() {
        BookCreateDTO createDTO = new BookCreateDTO();
        createDTO.setName("Новая книга");
        createDTO.setGenreId(1L);
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Фантастика");
        when(genreRepository.findById(createDTO.getGenreId())).thenReturn(Optional.of(genre));
        Book savedBook = Book.builder()
                .id(100L)
                .name(createDTO.getName())
                .genre(genre)
                .build();
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        BookDTO result = bookService.createBook(createDTO);
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Новая книга", result.getName());
        assertEquals("Фантастика", result.getGenre());
        verify(genreRepository).findById(createDTO.getGenreId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void testUpdateBookSuccess() {
        Long bookId = 1L;
        Long genreId = 2L;
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setId(bookId);
        updateDTO.setName("Обновленное название");
        updateDTO.setGenreId(genreId);
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName("Фантастика");
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setName("Старая книга");
        existingBook.setGenre(new Genre());
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        BookDTO result = bookService.updateBook(updateDTO);
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Обновленное название", result.getName());
        assertEquals("Фантастика", result.getGenre());
        verify(bookRepository).findById(bookId);
        verify(genreRepository).findById(genreId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void testUpdateBookNotFound() {
        Long bookId = 1L;
        BookUpdateDTO updateDTO = new BookUpdateDTO();
        updateDTO.setId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            bookService.updateBook(updateDTO);
        });
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 123L;
        bookService.deleteBook(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void testGetAllBooks() {
        // Создаем Genre и Author для тестовых данных
        Genre genre1 = new Genre(1L, "Genre 1", new HashSet<>());
        Genre genre2 = new Genre(2L, "Genre 2", new HashSet<>());
        Author author1 = new Author(1L, "Author 1 name", "Author 1 surname", new HashSet<>());
        Author author2 = new Author(2L, "Author 2 name", "Author 2 surname", new HashSet<>());
        Book book1 = Book.builder()
                .id(101L)
                .name("Book One")
                .genre(genre1)
                .authors(new HashSet<>(Arrays.asList(author1)))
                .build();
        Book book2 = Book.builder()
                .id(102L)
                .name("Book Two")
                .genre(genre2)
                .authors(new HashSet<>(Arrays.asList(author2)))
                .build();
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);
        BookServiceImpl spyService = spy(bookService);
        doAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return new BookDTO(
                    book.getId(),
                    book.getName(),
                    book.getGenre().getName(),
                    book.getAuthors().stream()
                            .map(a -> new AuthorDTO(a.getId(), a.getName(), a.getSurname(), new ArrayList<>()))
                            .toList()
            );
        }).when(spyService).convertEntityToDto(any(Book.class));
        List<BookDTO> result = spyService.getAllBooks();
        assertNotNull(result);
        assertEquals(2, result.size());
        BookDTO dto1 = result.get(0);
        assertEquals(101L, dto1.getId());
        assertEquals("Book One", dto1.getName());
        assertEquals("Genre 1", dto1.getGenre());
        assertEquals(1, dto1.getAuthors().size());
        assertEquals("Author 1 name", dto1.getAuthors().get(0).getName());
        BookDTO dto2 = result.get(1);
        assertEquals(102L, dto2.getId());
        assertEquals("Book Two", dto2.getName());
        assertEquals("Genre 2", dto2.getGenre());
        assertEquals(1, dto2.getAuthors().size());
        assertEquals("Author 2 name", dto2.getAuthors().get(0).getName());
        verify(bookRepository).findAll();
        verify(spyService, times(2)).convertEntityToDto(any(Book.class));
    }
}
