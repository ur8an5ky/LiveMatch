package pl.ur8an5ky.livematch.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.dto.MatchDto;
import pl.ur8an5ky.livematch.dto.MatchStatusChangeDto;
import pl.ur8an5ky.livematch.exception.BusinessRuleViolationException;
import pl.ur8an5ky.livematch.exception.ResourceNotFoundException;
import pl.ur8an5ky.livematch.mapper.MatchMapper;
import pl.ur8an5ky.livematch.repository.MatchRepository;
import pl.ur8an5ky.livematch.aop.Auditable;

import java.util.List;

/**
 * Manages the match lifecycle: creation, status transitions, and deletion.
 * Publishes WebSocket events on every state-changing operation so that
 * subscribed clients can react in real time.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TeamService teamService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Returns all matches, optionally filtered by status.
     *
     * @param statusFilter optional status filter; if {@code null}, returns all matches
     * @return list of matching matches
     */
    @Transactional(readOnly = true)
    public List<MatchDto> getAll(MatchStatus statusFilter) {
        List<Match> matches = (statusFilter != null)
                ? matchRepository.findByStatus(statusFilter)
                : matchRepository.findAll();
        return matches.stream().map(matchMapper::toDto).toList();
    }

    /**
     * Returns a single match by its identifier.
     *
     * @param id match identifier
     * @return match details
     * @throws ResourceNotFoundException if the match does not exist
     */
    @Transactional(readOnly = true)
    public MatchDto getById(Long id) {
        return matchMapper.toDto(findOrThrow(id));
    }

    /**
     * Creates a new match between two distinct teams and publishes a
     * {@code /topic/matches/created} WebSocket event. Audited via {@link Auditable}.
     *
     * @param dto creation payload (home team id, away team id, start time)
     * @return persisted match
     * @throws BusinessRuleViolationException if home and away team are the same
     * @throws ResourceNotFoundException      if either team does not exist
     */
    @Auditable
    public MatchDto create(MatchCreateDto dto) {
        if (dto.homeTeamId().equals(dto.awayTeamId())) {
            throw new BusinessRuleViolationException(
                    "A team cannot play against itself");
        }
        Match match = Match.builder()
                .homeTeam(teamService.findOrThrow(dto.homeTeamId()))
                .awayTeam(teamService.findOrThrow(dto.awayTeamId()))
                .startTime(dto.startTime())
                .build();

        MatchDto result = matchMapper.toDto(matchRepository.save(match));

        messagingTemplate.convertAndSend("/topic/matches/created", result);

        return result;
    }

    /**
     * Transitions a match to a new status and broadcasts the change on the
     * {@code /topic/matches/{id}/status} WebSocket channel. Validates that
     * the transition is allowed (terminal states cannot be changed).
     * Audited via {@link Auditable}.
     *
     * @param id        match identifier
     * @param newStatus desired status
     * @return updated match
     * @throws BusinessRuleViolationException if the match is already finished or cancelled
     * @throws ResourceNotFoundException      if the match does not exist
     */
    @Auditable
    public MatchDto updateStatus(Long id, MatchStatus newStatus) {
        Match match = findOrThrow(id);
        MatchStatus previousStatus = match.getStatus();
        validateStatusTransition(previousStatus, newStatus);
        match.setStatus(newStatus);

        MatchStatusChangeDto notification = new MatchStatusChangeDto(
                id, previousStatus, newStatus, LocalDateTime.now()
        );
        messagingTemplate.convertAndSend(
                "/topic/matches/" + id + "/status",
                notification
        );

        return matchMapper.toDto(match);
    }

    /**
     * Deletes a match by its identifier. Audited via {@link Auditable}.
     *
     * @param id match identifier
     * @throws ResourceNotFoundException if the match does not exist
     */
    @Auditable
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match", id);
        }
        matchRepository.deleteById(id);
    }

    /**
     * Loads a match entity or throws if it does not exist.
     * Used internally by this service and by {@link MatchEventService}.
     *
     * @param id match identifier
     * @return loaded entity
     * @throws ResourceNotFoundException if the match does not exist
     */
    public Match findOrThrow(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    /**
     * Rejects transitions from terminal states (FINISHED, CANCELLED).
     * All other transitions are currently accepted.
     */
    private void validateStatusTransition(MatchStatus from, MatchStatus to) {
        if (from == MatchStatus.FINISHED || from == MatchStatus.CANCELLED) {
            throw new BusinessRuleViolationException(
                    "Cannot change the status of a finished or cancelled match");
        }
    }
}