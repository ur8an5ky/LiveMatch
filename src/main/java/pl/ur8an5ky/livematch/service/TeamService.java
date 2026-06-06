package pl.ur8an5ky.livematch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ur8an5ky.livematch.domain.Team;
import pl.ur8an5ky.livematch.dto.TeamCreateDto;
import pl.ur8an5ky.livematch.dto.TeamDto;
import pl.ur8an5ky.livematch.exception.ResourceNotFoundException;
import pl.ur8an5ky.livematch.mapper.TeamMapper;
import pl.ur8an5ky.livematch.repository.TeamRepository;
import pl.ur8an5ky.livematch.aop.Auditable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Transactional(readOnly = true)
    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamDto getById(Long id) {
        return teamMapper.toDto(findOrThrow(id));
    }

    @Auditable
    public TeamDto create(TeamCreateDto dto) {
        Team team = teamMapper.fromCreateDto(dto);
        Team saved = teamRepository.save(team);
        return teamMapper.toDto(saved);
    }

    @Auditable
    public TeamDto update(Long id, TeamCreateDto dto) {
        Team team = findOrThrow(id);
        teamMapper.updateFromDto(team, dto);
        return teamMapper.toDto(team);
    }

    @Auditable
    public void delete(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team", id);
        }
        teamRepository.deleteById(id);
    }

    /**
     * Package-internal helper — also used by MatchService.
     */
    public Team findOrThrow(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
    }
}