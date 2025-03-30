package ru.itgirl.library2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.library2.DTO.GenreDTO;
import ru.itgirl.library2.service.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genre/{id}")
    GenreDTO getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }
}
