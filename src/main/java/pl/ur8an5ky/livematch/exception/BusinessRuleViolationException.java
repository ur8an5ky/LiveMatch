package pl.ur8an5ky.livematch.exception;

/**
 * Thrown when a request is syntactically valid but violates business rules
 * (e.g. attempting to add an event to a finished match).
 * The global handler translates it into HTTP 400.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}