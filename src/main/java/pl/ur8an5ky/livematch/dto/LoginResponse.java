package pl.ur8an5ky.livematch.dto;

import pl.ur8an5ky.livematch.domain.Role;

/**
 * Response returned after successful authentication.
 * Contains the JWT token along with user metadata used by the SPA client.
 */
public record LoginResponse(
        String token,
        String username,
        Role role,
        long expiresInMs
) {}