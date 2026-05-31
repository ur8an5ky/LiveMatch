package pl.ur8an5ky.livematch.domain;

/**
 * TStatus of a match in its lifecycle.
 */
public enum MatchStatus {
    SCHEDULED,   // scheduled, not started yet
    LIVE,        // in progress
    HALF_TIME,   // halftime
    FINISHED,    // finished
    CANCELLED    // canceled
}