package pl.ur8an5ky.livematch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO received when creating/editing a team (POST/PUT /api/teams).
 * No id field — id is assigned by the database.
 */
public record TeamCreateDto(
        @NotBlank(message = "Team name is required")
        @Size(max = 100, message = "Team name can be at most 100 characters")
        String name,

        @Size(max = 10, message = "Short name can be at most 10 characters")
        String shortName,

        @Size(max = 50)
        String country
) {}