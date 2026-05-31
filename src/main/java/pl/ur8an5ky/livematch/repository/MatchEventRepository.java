package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.MatchEvent;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {

    List<MatchEvent> findByMatchIdOrderByOccurredAtAsc(Long matchId);
}