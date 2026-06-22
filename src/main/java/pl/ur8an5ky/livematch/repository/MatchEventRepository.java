package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.MatchEvent;

import java.util.List;

/**
 * JPA repository for in-match events (goals, cards, period markers).
 */
@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    /** Returns all events of the given match ordered chronologically. */
    List<MatchEvent> findByMatchIdOrderByOccurredAtAsc(Long matchId);
}