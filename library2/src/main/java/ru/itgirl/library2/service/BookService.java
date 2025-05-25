package ru.itgirl.library2.service;

import ru.itgirl.library2.dto.BookCreateDTO;
import ru.itgirl.library2.dto.BookDTO;
import ru.itgirl.library2.dto.BookUpdateDTO;

import java.util.List;

public interface BookService {
    BookDTO getByNameV1(String name);

    BookDTO getByNameV2(String name);

    BookDTO getByNameV3(String name);

    BookDTO createBook(BookCreateDTO bookCreateDTO);

    BookDTO updateBook(BookUpdateDTO bookUpdateDTO);

    void deleteBook(Long id);

    List<BookDTO> getAllBooks();
}
