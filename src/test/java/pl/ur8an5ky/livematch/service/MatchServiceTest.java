package pl.ur8an5ky.livematch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.domain.Team;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.exception.BusinessRuleViolationException;
import pl.ur8an5ky.livematch.mapper.MatchMapper;
import pl.ur8an5ky.livematch.repository.MatchRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private MatchMapper matchMapper;
    @Mock private TeamService teamService;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks private MatchService matchService;

    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setUp() {
        homeTeam = Team.builder().id(1L).name("Home").build();
        awayTeam = Team.builder().id(2L).name("Away").build();
    }

    @Test
    void shouldThrowBusinessRule_whenSameHomeAndAwayTeam() {
        MatchCreateDto dto = new MatchCreateDto(1L, 1L, LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> matchService.create(dto))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("itself");

        verify(matchRepository, never()).save(any());
    }

    @Test
    void shouldBroadcastStatusChange_whenStatusUpdated() {
        Match match = Match.builder()
                .id(10L).homeTeam(homeTeam).awayTeam(awayTeam)
                .status(MatchStatus.SCHEDULED)
                .build();
        when(matchRepository.findById(10L)).thenReturn(Optional.of(match));

        matchService.updateStatus(10L, MatchStatus.LIVE);

        verify(messagingTemplate).convertAndSend(eq("/topic/matches/10/status"), any(Object.class));
    }

    @Test
    void shouldThrowBusinessRule_whenChangingFinishedMatchStatus() {
        Match finishedMatch = Match.builder()
                .id(5L).homeTeam(homeTeam).awayTeam(awayTeam)
                .status(MatchStatus.FINISHED)
                .build();
        when(matchRepository.findById(5L)).thenReturn(Optional.of(finishedMatch));

        assertThatThrownBy(() -> matchService.updateStatus(5L, MatchStatus.LIVE))
                .isInstanceOf(BusinessRuleViolationException.class);

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }
}