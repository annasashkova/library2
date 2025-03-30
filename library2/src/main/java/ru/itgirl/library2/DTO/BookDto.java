package ru.itgirl.library2.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookDto {
    private Long id;
    private String name;
    private String genre;
    private List<AuthorDto> authors;
}
