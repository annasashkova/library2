package ru.itgirl.library2.service;

import ru.itgirl.library2.dto.BookCreateDTO;
import ru.itgirl.library2.dto.BookDto;
import ru.itgirl.library2.dto.BookUpdateDTO;

import java.util.List;

public interface BookService {
    BookDto getByNameV1(String name);

    BookDto getByNameV2(String name);

    BookDto getByNameV3(String name);

    BookDto createBook(BookCreateDTO bookCreateDTO);

    BookDto updateBook(BookUpdateDTO bookUpdateDTO);

    void deleteBook(Long id);

    List<BookDto> getAllBooks();
}
