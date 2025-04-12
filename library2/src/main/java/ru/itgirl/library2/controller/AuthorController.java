package ru.itgirl.library2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.library2.DTO.AuthorDto;
import ru.itgirl.library2.service.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/author")
    AuthorDto getAuthorByName(@RequestParam("name")String name) {
        return authorService.getAuthorByName(name);
    }

    @GetMapping("/author/v2")
    AuthorDto getAuthorByNameBySQL(@RequestParam("name")String name) {
        return authorService.getAuthorByNameV2(name);
    }

    @GetMapping("/author/v3")
    AuthorDto getAuthorByNameV3(@RequestParam("name")String name) {
        return authorService.getAuthorByNameV3(name);
    }
}
