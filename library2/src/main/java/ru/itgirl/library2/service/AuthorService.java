package ru.itgirl.library2.service;

import ru.itgirl.library2.dto.AuthorCreateDTO;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.AuthorUpdateDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO getAuthorById(Long id);

    AuthorDTO getAuthorByName(String name);

    AuthorDTO getAuthorByNameV2(String name);

    AuthorDTO getAuthorByNameV3(String name);

    AuthorDTO createAuthor(AuthorCreateDTO authorCreateDto);

    AuthorDTO updateAuthor(AuthorUpdateDTO authorUpdateDto);

    void deleteAuthor(Long id);

    List<AuthorDTO> getAllAuthors();
}
