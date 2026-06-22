package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.User;

import java.util.Optional;

/**
 * JPA repository for application user accounts (administrators).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /** Finds a user by their unique username. */
    Optional<User> findByUsername(String username);

    /** Returns true if a user with the given username already exists. */
    boolean existsByUsername(String username);
}