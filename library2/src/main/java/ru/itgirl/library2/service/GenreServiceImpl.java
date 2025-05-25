package ru.itgirl.library2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.BookDTO;
import ru.itgirl.library2.dto.GenreDTO;
import ru.itgirl.library2.model.Genre;
import ru.itgirl.library2.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreDTO getGenreById(Long id) {
        log.info("Try to find genre by id: " + id);
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            GenreDTO genreDto = convertToDto(genre.get());
            log.info("Genre with id {}: {}", id, genreDto.toString());
            return genreDto;
        } else {
            log.error("Genre with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    private GenreDTO convertToDto(Genre genre) {
        List<BookDTO> bookDtoList = genre.getBooks()
                .stream()
                .map(book -> BookDTO.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .authors(book.getAuthors().stream()
                                .map(author -> AuthorDTO.builder()
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
