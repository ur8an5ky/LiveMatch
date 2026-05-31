package pl.ur8an5ky.livematch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchStatus;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByStatusIn(List<MatchStatus> statuses);
}