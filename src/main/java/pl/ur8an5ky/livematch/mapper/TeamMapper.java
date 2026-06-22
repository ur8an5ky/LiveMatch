package pl.ur8an5ky.livematch.mapper;

import org.springframework.stereotype.Component;
import pl.ur8an5ky.livematch.domain.Team;
import pl.ur8an5ky.livematch.dto.TeamCreateDto;
import pl.ur8an5ky.livematch.dto.TeamDto;

/**
 * Conversions between the Team entity and its DTOs.
 */
@Component
public class TeamMapper {

    /** Converts a Team entity to its DTO representation. */
    public TeamDto toDto(Team team) {
        if (team == null) return null;
        return new TeamDto(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getCountry()
        );
    }

    /** Builds a new Team entity from a creation DTO (for POST). */
    public Team fromCreateDto(TeamCreateDto dto) {
        return Team.builder()
                .name(dto.name())
                .shortName(dto.shortName())
                .country(dto.country())
                .build();
    }

    /**
     * Updates an existing entity with values from the DTO (for PUT).
     */
    public void updateFromDto(Team team, TeamCreateDto dto) {
        team.setName(dto.name());
        team.setShortName(dto.shortName());
        team.setCountry(dto.country());
    }
}