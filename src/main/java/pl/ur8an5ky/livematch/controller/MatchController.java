package pl.ur8an5ky.livematch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.dto.MatchDto;
import pl.ur8an5ky.livematch.dto.MatchStatusUpdateDto;
import pl.ur8an5ky.livematch.service.MatchService;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public List<MatchDto> getAll(@RequestParam(required = false) MatchStatus status) {
        return matchService.getAll(status);
    }

    @GetMapping("/{id}")
    public MatchDto getById(@PathVariable Long id) {
        return matchService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchDto create(@Valid @RequestBody MatchCreateDto dto) {
        return matchService.create(dto);
    }

    @PatchMapping("/{id}/status")
    public MatchDto updateStatus(@PathVariable Long id,
                                 @Valid @RequestBody MatchStatusUpdateDto dto) {
        return matchService.updateStatus(id, dto.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}