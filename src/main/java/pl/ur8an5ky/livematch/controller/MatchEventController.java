package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.ur8an5ky.livematch.dto.MatchEventCreateDto;
import pl.ur8an5ky.livematch.dto.MatchEventDto;
import pl.ur8an5ky.livematch.service.MatchEventService;

import java.util.List;

@RestController
@RequestMapping("/api/matches/{matchId}/events")
@RequiredArgsConstructor
public class MatchEventController {

    private final MatchEventService eventService;

    @GetMapping
    public List<MatchEventDto> getByMatch(@PathVariable Long matchId) {
        return eventService.getByMatchId(matchId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchEventDto add(@PathVariable Long matchId,
                             @Valid @RequestBody MatchEventCreateDto dto) {
        return eventService.addEvent(matchId, dto);
    }
}