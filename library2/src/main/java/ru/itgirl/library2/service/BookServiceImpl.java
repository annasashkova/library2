package ru.itgirl.library2.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library2.dto.BookCreateDTO;
import ru.itgirl.library2.dto.BookDto;
import ru.itgirl.library2.dto.BookUpdateDTO;
import ru.itgirl.library2.model.Book;
import ru.itgirl.library2.repository.BookRepository;
import ru.itgirl.library2.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return convertEntityToDto(book.get());
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return convertEntityToDto(book.get());
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV3(String name) {
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findOne(specification);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return convertEntityToDto(book.get());
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto createBook(BookCreateDTO bookCreateDTO) {
        log.info("Try to create book");
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDTO));
        BookDto bookDto = convertEntityToDto(book);
        log.info("Book created: {}", bookDto.toString());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookUpdateDTO bookUpdateDTO) {
        log.info("Try to find book by id {}", bookUpdateDTO.getId());
        Optional<Book> book = bookRepository.findById(bookUpdateDTO.getId());
        if (book.isPresent()) {
            book.get().setName(bookUpdateDTO.getName());
            book.get().setGenre(genreRepository.findById(bookUpdateDTO.getGenreId()).get());
            Book savedBook = bookRepository.save(book.get());
            BookDto bookDto = convertEntityToDto(savedBook);
            log.info("Book with id {} updated: {}", bookUpdateDTO.getId(), bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with id {} not found", bookUpdateDTO.getId());
            throw new NoSuchElementException("No value present");
        }

    }

    @Override
    public void deleteBook(Long id) {
        log.info("Try to find book by id {}", id);
        bookRepository.deleteById(id);
        log.info("Book with id {} deleted", id);
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Try to find all books");
        List<Book> books = bookRepository.findAll();
        log.info("Found {} books {}", books.size(), books);
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private Book convertDtoToEntity(BookCreateDTO bookCreateDTO) {
        return Book.builder().
                name(bookCreateDTO.getName()).
                genre(genreRepository.findById(bookCreateDTO.getGenreId()).get()).
                build();
    }

    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
    }
}
