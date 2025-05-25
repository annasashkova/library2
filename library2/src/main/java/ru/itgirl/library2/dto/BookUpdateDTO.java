package ru.itgirl.library2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class BookUpdateDTO {
    @NotNull(message = "Необходимо указать id книги")
    private Long id;
    @Size(min = 3, max = 10)
    @NotBlank(message = "Необходимо указать имя")
    private String name;
    @NotNull (message = "Необходимо указать id жанра")
    private Long genreId;
}
