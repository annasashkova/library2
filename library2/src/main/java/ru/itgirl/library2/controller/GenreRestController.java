package ru.itgirl.library2.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.library2.dto.GenreDTO;
import ru.itgirl.library2.service.GenreService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "library-users")
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/genre/{id}")
    GenreDTO getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }
}
