package pl.ur8an5ky.livematch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ur8an5ky.livematch.domain.Team;
import pl.ur8an5ky.livematch.dto.TeamCreateDto;
import pl.ur8an5ky.livematch.dto.TeamDto;
import pl.ur8an5ky.livematch.exception.ResourceNotFoundException;
import pl.ur8an5ky.livematch.mapper.TeamMapper;
import pl.ur8an5ky.livematch.repository.TeamRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private TeamMapper teamMapper;

    @InjectMocks private TeamService teamService;

    private Team sampleTeam;
    private TeamDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleTeam = Team.builder()
                .id(1L).name("Test FC").shortName("TFC").country("Testland")
                .build();
        sampleDto = new TeamDto(1L, "Test FC", "TFC", "Testland");
    }

    @Test
    void shouldReturnAllTeams() {
        when(teamRepository.findAll()).thenReturn(List.of(sampleTeam));
        when(teamMapper.toDto(sampleTeam)).thenReturn(sampleDto);

        List<TeamDto> result = teamService.getAll();

        assertThat(result).hasSize(1).containsExactly(sampleDto);
        verify(teamRepository).findAll();
    }

    @Test
    void shouldReturnTeam_whenExistingId() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(sampleTeam));
        when(teamMapper.toDto(sampleTeam)).thenReturn(sampleDto);

        TeamDto result = teamService.getById(1L);

        assertThat(result).isEqualTo(sampleDto);
    }

    @Test
    void shouldThrowNotFound_whenNonExistentId() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Team")
                .hasMessageContaining("99");
    }

    @Test
    void shouldCreateAndReturnTeam() {
        TeamCreateDto createDto = new TeamCreateDto("Test FC", "TFC", "Testland");
        when(teamMapper.fromCreateDto(createDto)).thenReturn(sampleTeam);
        when(teamRepository.save(sampleTeam)).thenReturn(sampleTeam);
        when(teamMapper.toDto(sampleTeam)).thenReturn(sampleDto);

        TeamDto result = teamService.create(createDto);

        assertThat(result).isEqualTo(sampleDto);
        verify(teamRepository).save(sampleTeam);
    }

    @Test
    void shouldThrowNotFound_whenDeletingNonExistentTeam() {
        when(teamRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> teamService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(teamRepository, never()).deleteById(any());
    }
}