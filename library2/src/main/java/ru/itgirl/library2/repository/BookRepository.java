package ru.itgirl.library2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.itgirl.library2.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findBookByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM BOOK WHERE name = ?")
    Optional<Book> findBookByNameBySql(String name);
}
