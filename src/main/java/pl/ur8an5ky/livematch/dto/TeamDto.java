package pl.ur8an5ky.livematch.dto;

/**
 * DTO of a team returned in API responses.
 */
public record TeamDto(
        Long id,
        String name,
        String shortName,
        String country
) {}