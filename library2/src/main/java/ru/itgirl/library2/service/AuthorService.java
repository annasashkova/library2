package ru.itgirl.library2.service;

import ru.itgirl.library2.DTO.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorByName(String name);
    AuthorDto getAuthorByNameV2(String name);
    AuthorDto getAuthorByNameV3(String name);
}
