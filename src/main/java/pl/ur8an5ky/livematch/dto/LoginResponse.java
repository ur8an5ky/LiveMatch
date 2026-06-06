package pl.ur8an5ky.livematch.dto;

import pl.ur8an5ky.livematch.domain.Role;

public record LoginResponse(
        String token,
        String username,
        Role role,
        long expiresInMs
) {}