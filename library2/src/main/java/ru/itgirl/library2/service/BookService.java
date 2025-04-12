package ru.itgirl.library2.service;

import ru.itgirl.library2.DTO.BookDto;

public interface BookService {
    BookDto getByNameV1(String name);
    BookDto getByNameV2(String name);
    BookDto getByNameV3(String name);
}
