package ru.itgirl.library2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itgirl.library2.DTO.AuthorDto;
import ru.itgirl.library2.DTO.BookDto;
import ru.itgirl.library2.DTO.GenreDTO;
import ru.itgirl.library2.model.Genre;
import ru.itgirl.library2.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        return convertToDto(genre);
    }

    private GenreDTO convertToDto(Genre genre) {
        List<BookDto> bookDtoList = genre.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .authors(book.getAuthors().stream()
                                .map(author -> AuthorDto.builder()
                                        .id(author.getId())
                                        .name(author.getName())
                                        .surname(author.getSurname())
                                        .build())
                                .toList())
                        .build())
                .toList();
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(bookDtoList)
                .build();
    }
}
