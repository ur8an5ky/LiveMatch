package pl.ur8an5ky.livematch.dto;

import pl.ur8an5ky.livematch.domain.MatchStatus;
import java.time.LocalDateTime;

/**
 * DTO of a match in API responses.
 * Teams are embedded as nested DTOs — readable for the client.
 */
public record MatchDto(
        Long id,
        TeamDto homeTeam,
        TeamDto awayTeam,
        LocalDateTime startTime,
        MatchStatus status,
        Integer homeScore,
        Integer awayScore
) {}