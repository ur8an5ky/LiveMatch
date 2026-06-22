package pl.ur8an5ky.livematch.mapper;

import org.springframework.stereotype.Component;
import pl.ur8an5ky.livematch.domain.MatchEvent;
import pl.ur8an5ky.livematch.dto.MatchEventDto;

/**
 * Converts the MatchEvent entity to its DTO representation.
 */
@Component
public class MatchEventMapper {

    public MatchEventDto toDto(MatchEvent event) {
        if (event == null) return null;
        return new MatchEventDto(
                event.getId(),
                event.getMatch().getId(),
                event.getEventType(),
                event.getMinuteOfMatch(),
                event.getTeam() != null ? event.getTeam().getId() : null,
                event.getTeam() != null ? event.getTeam().getName() : null,
                event.getDescription(),
                event.getOccurredAt()
        );
    }
}