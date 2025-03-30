package ru.itgirl.library2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library2.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
