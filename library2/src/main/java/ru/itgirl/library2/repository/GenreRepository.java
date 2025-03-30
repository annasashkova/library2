package ru.itgirl.library2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library2.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
