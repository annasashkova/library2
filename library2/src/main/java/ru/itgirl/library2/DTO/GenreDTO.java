package ru.itgirl.library2.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenreDTO {
    Long id;
    String name;
    List<BookDto> books;
}
