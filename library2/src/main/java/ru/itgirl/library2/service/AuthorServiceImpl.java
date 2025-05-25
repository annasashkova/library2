package ru.itgirl.library2.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library2.dto.AuthorCreateDTO;
import ru.itgirl.library2.dto.AuthorDTO;
import ru.itgirl.library2.dto.AuthorUpdateDTO;
import ru.itgirl.library2.dto.BookDTO;
import ru.itgirl.library2.model.Author;
import ru.itgirl.library2.repository.AuthorRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDTO getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDTO authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDTO getAuthorByName(String name) {
        log.info("Try to find author by name {}", name);
        Optional<Author> author = authorRepository.findAuthorByName(name);
        if (author.isPresent()) {
            AuthorDTO authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found",name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDTO getAuthorByNameV2(String name) {
        log.info("Try to find author by name {}", name);
        Optional<Author> author = authorRepository.findAuthorByNameBySql(name);
        if (author.isPresent()) {
            AuthorDTO authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found",name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDTO getAuthorByNameV3(String name) {
        Specification<Author> specification = Specification.where(new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });
        log.info("Try to find author by name {}", name);
        Optional<Author> author =  authorRepository.findOne(specification);
        if (author.isPresent()) {
            AuthorDTO authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found",name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDTO createAuthor(AuthorCreateDTO authorCreateDto) {
        log.info("Try to create author");
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDTO authorDto = convertEntityToDto(author);
        log.info("Author created: {}", authorDto.toString());
        return authorDto;
    }

    @Override
    public AuthorDTO updateAuthor(AuthorUpdateDTO authorUpdateDto) {
        log.info("Try to find author by id {}", authorUpdateDto.getId());
        Optional<Author> author = authorRepository.findById(authorUpdateDto.getId());
        if (author.isPresent()) {
            author.get().setName(authorUpdateDto.getName());
            author.get().setSurname(authorUpdateDto.getSurname());
            Author savedAuthor = authorRepository.save(author.get());
            AuthorDTO authorDto = convertEntityToDto(savedAuthor);
            log.info("Author with id {} updated: {}", authorUpdateDto.getId(),authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", authorUpdateDto.getId());
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Try to find author by id {}", id);
        authorRepository.deleteById(id);
        log.info("Author with id {} deleted", id);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        log.info("Try to find all authors");
        List<Author> authors = authorRepository.findAll();
        log.info("All authors: {}",authors);
        return authors.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private Author convertDtoToEntity(AuthorCreateDTO authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }

    public AuthorDTO convertEntityToDto(Author author) {
        if (author == null) {
            return null;
        }
        List<BookDTO> bookDtoList = new ArrayList<>();
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDTO.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();
        }
        AuthorDTO authorDto = AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
        return authorDto;
    }


    private AuthorDTO convertToDto(Author author) {
        List<BookDTO> bookDtoList = author.getBooks()
                .stream()
                .map(book -> BookDTO.builder()
                        .genre(book.getGenre().getName())
                        .name(book.getName())
                        .id(book.getId())
                        .build()
                ).toList();
        return AuthorDTO.builder()
                .books(bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

}
