package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.dto.MatchDto;
import pl.ur8an5ky.livematch.dto.MatchStatusUpdateDto;
import pl.ur8an5ky.livematch.service.MatchService;

import java.util.List;

/**
 * REST controller for managing football matches.
 * Provides read access to all clients (public) and write operations
 * (create, update status, delete) to administrators only.
 * The status update is exposed as a dedicated PATCH endpoint because
 * the match lifecycle is the primary driver of real-time WebSocket events.
 */
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * Returns the list of matches, optionally filtered by status.
     *
     * @param status optional status filter (SCHEDULED, LIVE, HALF_TIME, FINISHED, CANCELLED)
     * @return list of matches matching the filter, or all matches if filter is omitted
     */
    @GetMapping
    public List<MatchDto> getAll(@RequestParam(required = false) MatchStatus status) {
        return matchService.getAll(status);
    }

    /**
     * Returns a single match by its identifier.
     *
     * @param id match identifier
     * @return match details
     * @throws pl.ur8an5ky.livematch.exception.ResourceNotFoundException
     *         if no match with the given id exists
     */
    @GetMapping("/{id}")
    public MatchDto getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    /**
     * Creates a new match. Admin-only endpoint.
     * Publishes a {@code /topic/matches/created} WebSocket event after persisting.
     *
     * @param dto creation payload (home team id, away team id, start time)
     * @return the persisted match with generated id
     * @throws pl.ur8an5ky.livematch.exception.BusinessRuleViolationException
     *         if the same team is used for both home and away
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchDto create(@Valid @RequestBody MatchCreateDto dto) {
        return matchService.create(dto);
    }

    /**
     * Updates the status of a match (e.g. starting or finishing it). Admin-only.
     * Publishes a {@code /topic/matches/{id}/status} WebSocket event so that all
     * subscribed clients update their UI immediately.
     *
     * @param id  match identifier
     * @param dto status update payload (target status)
     * @return updated match
     * @throws pl.ur8an5ky.livematch.exception.BusinessRuleViolationException
     *         if the status transition is not allowed (e.g. finished -> live)
     */
    @PatchMapping("/{id}/status")
    public MatchDto updateStatus(@PathVariable Long id,
                                 @Valid @RequestBody MatchStatusUpdateDto dto) {
        return matchService.updateStatus(id, dto.status());
    }

    /**
     * Deletes a match by its identifier. Admin-only.
     *
     * @param id match identifier
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}