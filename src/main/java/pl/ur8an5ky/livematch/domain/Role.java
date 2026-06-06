package pl.ur8an5ky.livematch.domain;

/**
 * User roles within the system.
 * ROLE_ prefix is a Spring Security convention required for hasRole() checks.
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_USER
}