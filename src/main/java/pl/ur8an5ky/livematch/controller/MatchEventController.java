package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.ur8an5ky.livematch.dto.MatchEventCreateDto;
import pl.ur8an5ky.livematch.dto.MatchEventDto;
import pl.ur8an5ky.livematch.service.MatchEventService;

import java.util.List;

/**
 * REST controller for in-match events (goals, cards, substitutions, period markers).
 * Nested under {@code /api/matches/{matchId}/events} to reinforce the parent-child
 * relationship between a match and its events. Adding events is the primary trigger
 * for real-time updates broadcast over WebSocket to all subscribed fans.
 */
@RestController
@RequestMapping("/api/matches/{matchId}/events")
@RequiredArgsConstructor
public class MatchEventController {

    private final MatchEventService eventService;

    /**
     * Returns all events recorded for the given match, ordered by occurrence.
     *
     * @param matchId match identifier
     * @return list of events (may be empty)
     * @throws pl.ur8an5ky.livematch.exception.ResourceNotFoundException
     *         if the match does not exist
     */
    @GetMapping
    public List<MatchEventDto> getByMatch(@PathVariable Long matchId) {
        return eventService.getByMatchId(matchId);
    }

    /**
     * Registers a new event for a live match. Admin-only endpoint.
     * Publishes a {@code /topic/matches/{matchId}/events} WebSocket message
     * so that subscribed fans see the event in real time.
     *
     * @param matchId match identifier
     * @param dto     event payload (type, team, minute, optional metadata)
     * @return the persisted event with generated id
     * @throws pl.ur8an5ky.livematch.exception.BusinessRuleViolationException
     *         if the match is not in a state that accepts events (e.g. SCHEDULED, FINISHED)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchEventDto add(@PathVariable Long matchId,
                             @Valid @RequestBody MatchEventCreateDto dto) {
        return eventService.addEvent(matchId, dto);
    }
}