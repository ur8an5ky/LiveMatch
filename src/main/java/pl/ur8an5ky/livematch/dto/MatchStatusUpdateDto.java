package pl.ur8an5ky.livematch.dto;

import jakarta.validation.constraints.NotNull;
import pl.ur8an5ky.livematch.domain.MatchStatus;

/**
 * Request body for changing a match status (PATCH /api/matches/{id}/status).
 * Separate endpoint for status — semantically cleaner than PUT on the whole resource.
 */
public record MatchStatusUpdateDto(
        @NotNull MatchStatus status
) {}