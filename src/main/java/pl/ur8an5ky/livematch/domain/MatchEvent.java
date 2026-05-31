package pl.ur8an5ky.livematch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Individual match events (goal, booking, substitution, whistle, etc.).
 * These are the events that the administrator enters in the control panel and which are sent
 * to fans via WebSocket.
 */
@Entity
@Table(name = "match_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private EventType eventType;

    /**
     * The minute of the match in which the event occurred (e.g. 23, 45+2 is recorded as 45).
     */
    @Column(name = "minute_of_match")
    private Integer minuteOfMatch;

    /**
     * The team to which the event relates (who scored the goal, who received the card).
     * May be null for general events (e.g. MATCH_START, HALF_TIME_END).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    /**
     * Optional text description (e.g. scorer's name, description of the situation).
     */
    @Column(length = 255)
    private String description;

    /**
     * The time the event was recorded in the system (not the match minute — this timestamp
     * indicates when the administrator entered the event into the system).
     */
    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @PrePersist
    public void prePersist() {
        if (occurredAt == null) occurredAt = LocalDateTime.now();
    }
}