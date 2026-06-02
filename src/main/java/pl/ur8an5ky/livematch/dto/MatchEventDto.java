package pl.ur8an5ky.livematch.dto;

import pl.ur8an5ky.livematch.domain.EventType;
import java.time.LocalDateTime;

/**
 * DTO of a match event in API responses and WebSocket broadcasts.
 */
public record MatchEventDto(
        Long id,
        Long matchId,
        EventType eventType,
        Integer minuteOfMatch,
        Long teamId,
        String teamName,
        String description,
        LocalDateTime occurredAt
) {}