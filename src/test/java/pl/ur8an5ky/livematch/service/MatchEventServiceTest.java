package pl.ur8an5ky.livematch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.ur8an5ky.livematch.domain.*;
import pl.ur8an5ky.livematch.dto.MatchEventCreateDto;
import pl.ur8an5ky.livematch.dto.MatchEventDto;
import pl.ur8an5ky.livematch.exception.BusinessRuleViolationException;
import pl.ur8an5ky.livematch.mapper.MatchEventMapper;
import pl.ur8an5ky.livematch.repository.MatchEventRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchEventServiceTest {

    @Mock private MatchEventRepository eventRepository;
    @Mock private MatchEventMapper eventMapper;
    @Mock private MatchService matchService;
    @Mock private TeamService teamService;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks private MatchEventService eventService;

    private Match liveMatch;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setUp() {
        homeTeam = Team.builder().id(1L).name("Home").build();
        awayTeam = Team.builder().id(2L).name("Away").build();
        liveMatch = Match.builder()
                .id(10L).homeTeam(homeTeam).awayTeam(awayTeam)
                .status(MatchStatus.LIVE)
                .homeScore(0).awayScore(0)
                .build();
    }

    @Test
    void shouldIncrementHomeScore_whenGoalForHomeTeam() {
        when(matchService.findOrThrow(10L)).thenReturn(liveMatch);
        when(teamService.findOrThrow(1L)).thenReturn(homeTeam);
        when(eventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(eventMapper.toDto(any())).thenReturn(mock(MatchEventDto.class));

        MatchEventCreateDto dto = new MatchEventCreateDto(
                EventType.GOAL, 23, 1L, "Testowski");

        eventService.addEvent(10L, dto);

        assertThat(liveMatch.getHomeScore()).isEqualTo(1);
        assertThat(liveMatch.getAwayScore()).isZero();
    }

    @Test
    void shouldBroadcastEvent_whenEventAdded() {
        when(matchService.findOrThrow(10L)).thenReturn(liveMatch);
        when(teamService.findOrThrow(1L)).thenReturn(homeTeam);
        when(eventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        MatchEventDto resultDto = new MatchEventDto(99L, 10L, EventType.GOAL,
                23, 1L, "Home", "desc", LocalDateTime.now());
        when(eventMapper.toDto(any())).thenReturn(resultDto);

        eventService.addEvent(10L,
                new MatchEventCreateDto(EventType.GOAL, 23, 1L, "desc"));

        verify(messagingTemplate).convertAndSend(
                eq("/topic/matches/10/events"), eq(resultDto));
    }

    @Test
    void shouldThrowBusinessRule_whenAddingEventToFinishedMatch() {
        liveMatch.setStatus(MatchStatus.FINISHED);
        when(matchService.findOrThrow(10L)).thenReturn(liveMatch);

        assertThatThrownBy(() -> eventService.addEvent(10L,
                new MatchEventCreateDto(EventType.GOAL, 90, 1L, null)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("finished or cancelled");

        verify(eventRepository, never()).save(any());
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }
}