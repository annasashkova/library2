package ru.itgirl.library2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library2.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
