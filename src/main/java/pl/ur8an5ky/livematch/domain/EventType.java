package pl.ur8an5ky.livematch.domain;

/**
 * Types of on-pitch incidents recorded during a match.
 */
public enum EventType {
    GOAL,             // goal
    YELLOW_CARD,      // yellow card
    RED_CARD,         // red card
    SUBSTITUTION,     // substitution
    HALF_TIME_START,  // end of the first half
    HALF_TIME_END,    // start of the second half
    MATCH_START,      // kick-off
    MATCH_END         // end of the match
}