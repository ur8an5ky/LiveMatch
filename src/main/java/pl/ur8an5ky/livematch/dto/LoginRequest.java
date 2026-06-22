package pl.ur8an5ky.livematch.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for the login endpoint. Both fields are required.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}