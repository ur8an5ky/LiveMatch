package pl.ur8an5ky.livematch.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.dto.MatchDto;

/**
 * Converts the Match entity to its DTO representation,
 * delegating team conversion to TeamMapper.
 */
@Component
@RequiredArgsConstructor
public class MatchMapper {

    private final TeamMapper teamMapper;

    public MatchDto toDto(Match match) {
        if (match == null) return null;
        return new MatchDto(
                match.getId(),
                teamMapper.toDto(match.getHomeTeam()),
                teamMapper.toDto(match.getAwayTeam()),
                match.getStartTime(),
                match.getStatus(),
                match.getHomeScore(),
                match.getAwayScore()
        );
    }
}