package pl.ur8an5ky.livematch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.ur8an5ky.livematch.dto.MatchCreateDto;
import pl.ur8an5ky.livematch.dto.MatchDto;
import pl.ur8an5ky.livematch.domain.MatchStatus;
import pl.ur8an5ky.livematch.security.JwtUtil;
import pl.ur8an5ky.livematch.service.MatchService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
@AutoConfigureMockMvc(addFilters = false)
class MatchControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private MatchService matchService;
    @MockitoBean private JwtUtil jwtUtil;

    @Test
    void shouldReturn200_andListOfMatches() throws Exception {
        when(matchService.getAll(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturn201_whenCreatingValidMatch() throws Exception {
        MatchCreateDto request = new MatchCreateDto(1L, 2L, LocalDateTime.now().plusDays(1));
        MatchDto response = new MatchDto(1L, null, null,
                request.startTime(), MatchStatus.SCHEDULED, 0, 0);

        when(matchService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn400_whenCreatingMatchWithMissingFields() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }
}