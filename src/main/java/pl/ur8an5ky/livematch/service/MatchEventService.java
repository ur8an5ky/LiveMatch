package pl.ur8an5ky.livematch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.ur8an5ky.livematch.domain.EventType;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchEvent;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.dto.MatchEventCreateDto;
import pl.ur8an5ky.livematch.dto.MatchEventDto;
import pl.ur8an5ky.livematch.exception.BusinessRuleViolationException;
import pl.ur8an5ky.livematch.mapper.MatchEventMapper;
import pl.ur8an5ky.livematch.repository.MatchEventRepository;
import pl.ur8an5ky.livematch.aop.Auditable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchEventService {

    private final MatchEventRepository eventRepository;
    private final MatchEventMapper eventMapper;
    private final MatchService matchService;
    private final TeamService teamService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(readOnly = true)
    public List<MatchEventDto> getByMatchId(Long matchId) {
        matchService.findOrThrow(matchId);
        return eventRepository.findByMatchIdOrderByOccurredAtAsc(matchId)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Auditable
    public MatchEventDto addEvent(Long matchId, MatchEventCreateDto dto) {
        Match match = matchService.findOrThrow(matchId);

        if (match.getStatus() == MatchStatus.FINISHED
                || match.getStatus() == MatchStatus.CANCELLED) {
            throw new BusinessRuleViolationException(
                    "Cannot add events to a finished or cancelled match");
        }

        MatchEvent event = MatchEvent.builder()
                .match(match)
                .eventType(dto.eventType())
                .minuteOfMatch(dto.minuteOfMatch())
                .team(dto.teamId() != null ? teamService.findOrThrow(dto.teamId()) : null)
                .description(dto.description())
                .build();

        // update the score if this is a goal
        if (dto.eventType() == EventType.GOAL && dto.teamId() != null) {
            if (dto.teamId().equals(match.getHomeTeam().getId())) {
                match.setHomeScore(match.getHomeScore() + 1);
            } else if (dto.teamId().equals(match.getAwayTeam().getId())) {
                match.setAwayScore(match.getAwayScore() + 1);
            }
        }

        MatchEvent savedEvent = eventRepository.save(event);
        MatchEventDto eventDto = eventMapper.toDto(savedEvent);

        // Broadcast the new event to all clients subscribed to this match
        String destination = "/topic/matches/" + matchId + "/events";
        messagingTemplate.convertAndSend(destination, eventDto);

        return eventDto;
    }
}