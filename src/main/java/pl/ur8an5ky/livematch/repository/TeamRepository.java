package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.Team;

import java.util.Optional;

/**
 * JPA repository for {@link pl.ur8an5ky.livematch.domain.Team} entities
 * (both clubs and national teams).
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);
}