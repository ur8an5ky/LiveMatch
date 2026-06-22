package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchStatus;

import java.util.List;

/**
 * JPA repository for {@link pl.ur8an5ky.livematch.domain.Match} entities.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    /** Returns all matches with the given lifecycle status. */
    List<Match> findByStatus(MatchStatus status);

    List<Match> findByStatusIn(List<MatchStatus> statuses);
}