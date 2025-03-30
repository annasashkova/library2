package ru.itgirl.library2.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AuthorDto {
    private Long id;
    private String name;
    private String surname;
    private List<BookDto> books;
}
