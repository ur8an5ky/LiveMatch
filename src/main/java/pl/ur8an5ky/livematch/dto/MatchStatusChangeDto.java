package pl.ur8an5ky.livematch.dto;

import pl.ur8an5ky.livematch.domain.MatchStatus;

import java.time.LocalDateTime;

/**
 * Notification sent over WebSocket when a match status changes.
 * Subscribed clients use it to update their UI (e.g. "Match started").
 */
public record MatchStatusChangeDto(
        Long matchId,
        MatchStatus previousStatus,
        MatchStatus newStatus,
        LocalDateTime changedAt
) {}