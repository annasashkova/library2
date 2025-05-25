package ru.itgirl.library2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorDTO {
    private Long id;
    private String name;
    private String surname;
    private List<BookDTO> books;
}
