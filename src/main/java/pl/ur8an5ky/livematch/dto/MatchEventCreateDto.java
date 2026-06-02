package pl.ur8an5ky.livematch.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.ur8an5ky.livematch.domain.EventType;

/**
 * Request body for adding an event to a match (POST /api/matches/{matchId}/events).
 * matchId is NOT in the body — we take it from the URL path.
 */
public record MatchEventCreateDto(
        @NotNull(message = "Event type is required")
        EventType eventType,

        @Min(value = 0, message = "Minute of match cannot be negative")
        Integer minuteOfMatch,

        Long teamId,

        @Size(max = 255)
        String description
) {}