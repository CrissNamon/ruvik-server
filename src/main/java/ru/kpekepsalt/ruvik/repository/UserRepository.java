package ru.kpekepsalt.ruvik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpekepsalt.ruvik.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByLogin(String login);
    Optional<User> findByToken(String token);
}
