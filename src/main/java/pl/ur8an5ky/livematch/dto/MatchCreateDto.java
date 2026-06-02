package pl.ur8an5ky.livematch.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Request body for creating a match.
 * We accept team IDs (not full objects) — the client fetches the team list separately.
 */
public record MatchCreateDto(
        @NotNull(message = "Home team ID is required")
        Long homeTeamId,

        @NotNull(message = "Away team ID is required")
        Long awayTeamId,

        @NotNull(message = "Start time is required")
        LocalDateTime startTime
) {}