package pl.ur8an5ky.livematch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a football match between two teams.
 * The central entity of the system - fans subscribe to matches via WebSocket,
 * and pitch events are assigned to matches.
 */
@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status;

    @Column(name = "home_score", nullable = false)
    private Integer homeScore;

    @Column(name = "away_score", nullable = false)
    private Integer awayScore;

    /**
     * Initialization of meaningful values for a newly created entity.
     * Called by JPA before inserting the record into the database.
     */
    @PrePersist
    public void prePersist() {
        if (status == null) status = MatchStatus.SCHEDULED;
        if (homeScore == null) homeScore = 0;
        if (awayScore == null) awayScore = 0;
    }
}