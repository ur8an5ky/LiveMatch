package pl.ur8an5ky.livematch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ur8an5ky.livematch.domain.Match;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.dto.MatchDto;
import pl.ur8an5ky.livematch.exception.BusinessRuleViolationException;
import pl.ur8an5ky.livematch.exception.ResourceNotFoundException;
import pl.ur8an5ky.livematch.mapper.MatchMapper;
import pl.ur8an5ky.livematch.repository.MatchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TeamService teamService;

    @Transactional(readOnly = true)
    public List<MatchDto> getAll(MatchStatus statusFilter) {
        List<Match> matches = (statusFilter != null)
                ? matchRepository.findByStatus(statusFilter)
                : matchRepository.findAll();
        return matches.stream().map(matchMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MatchDto getById(Long id) {
        return matchMapper.toDto(findOrThrow(id));
    }

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
        // status, homeScore, awayScore will be set by @PrePersist
        return matchMapper.toDto(matchRepository.save(match));
    }

    public MatchDto updateStatus(Long id, MatchStatus newStatus) {
        Match match = findOrThrow(id);
        validateStatusTransition(match.getStatus(), newStatus);
        match.setStatus(newStatus);
        return matchMapper.toDto(match);
    }

    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match", id);
        }
        matchRepository.deleteById(id);
    }

    public Match findOrThrow(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    /**
     * Simple validation of status transitions — e.g. you cannot reopen a finished match.
     */
    private void validateStatusTransition(MatchStatus from, MatchStatus to) {
        if (from == MatchStatus.FINISHED || from == MatchStatus.CANCELLED) {
            throw new BusinessRuleViolationException(
                    "Cannot change the status of a finished or cancelled match");
        }
    }
}