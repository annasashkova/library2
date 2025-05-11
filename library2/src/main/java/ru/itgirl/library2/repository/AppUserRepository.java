package ru.itgirl.library2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library2.model.AppUser;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
