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

/**
 * CRUD operations for teams (both clubs and national teams).
 * Write operations are audited via {@link Auditable}.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    /**
     * Returns all teams.
     *
     * @return list of teams (clubs and national teams)
     */
    @Transactional(readOnly = true)
    public List<TeamDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    /**
     * Returns a single team by its identifier.
     *
     * @param id team identifier
     * @return team details
     * @throws ResourceNotFoundException if the team does not exist
     */
    @Transactional(readOnly = true)
    public TeamDto getById(Long id) {
        return teamMapper.toDto(findOrThrow(id));
    }

    /**
     * Creates a new team. Audited via {@link Auditable}.
     *
     * @param dto creation payload
     * @return persisted team
     */
    @Auditable
    public TeamDto create(TeamCreateDto dto) {
        Team team = teamMapper.fromCreateDto(dto);
        Team saved = teamRepository.save(team);
        return teamMapper.toDto(saved);
    }

    /**
     * Updates an existing team. Audited via {@link Auditable}.
     *
     * @param id  team identifier
     * @param dto new team data
     * @return updated team
     * @throws ResourceNotFoundException if the team does not exist
     */
    @Auditable
    public TeamDto update(Long id, TeamCreateDto dto) {
        Team team = findOrThrow(id);
        teamMapper.updateFromDto(team, dto);
        return teamMapper.toDto(team);
    }

    /**
     * Deletes a team by its identifier. Audited via {@link Auditable}.
     *
     * @param id team identifier
     * @throws ResourceNotFoundException if the team does not exist
     */
    @Auditable
    public void delete(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team", id);
        }
        teamRepository.deleteById(id);
    }

    /**
     * Loads a team entity or throws if it does not exist.
     * Used internally and by {@link MatchService}.
     *
     * @param id team identifier
     * @return loaded entity
     * @throws ResourceNotFoundException if the team does not exist
     */
    public Team findOrThrow(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
    }
}