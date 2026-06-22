package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ur8an5ky.livematch.dto.TeamCreateDto;
import pl.ur8an5ky.livematch.dto.TeamDto;
import pl.ur8an5ky.livematch.service.TeamService;

import java.util.List;

/**
 * REST controller for managing teams.
 * A team can either be a club (with a name) or a national team (name is null,
 * identified solely by its ISO country code). Read operations are public;
 * mutations require an administrator role.
 */
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * Returns the full list of teams (clubs and national teams).
     *
     * @return list of all teams
     */
    @GetMapping
    public List<TeamDto> getAll() {
        return teamService.getAll();
    }

    /**
     * Returns a single team by its identifier.
     *
     * @param id team identifier
     * @return team details
     * @throws pl.ur8an5ky.livematch.exception.ResourceNotFoundException
     *         if no team with the given id exists
     */
    @GetMapping("/{id}")
    public TeamDto getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    /**
     * Creates a new team. Admin-only endpoint.
     *
     * @param dto creation payload (name optional, ISO country code required)
     * @return the persisted team with generated id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto create(@Valid @RequestBody TeamCreateDto dto) {
        return teamService.create(dto);
    }

    /**
     * Updates an existing team. Admin-only endpoint.
     *
     * @param id  team identifier
     * @param dto new team data (name, ISO country code)
     * @return updated team
     * @throws pl.ur8an5ky.livematch.exception.ResourceNotFoundException
     *         if the team does not exist
     */
    @PutMapping("/{id}")
    public TeamDto update(@PathVariable Long id,
                          @Valid @RequestBody TeamCreateDto dto) {
        return teamService.update(id, dto);
    }

    /**
     * Deletes a team by its identifier. Admin-only endpoint.
     *
     * @param id team identifier
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}