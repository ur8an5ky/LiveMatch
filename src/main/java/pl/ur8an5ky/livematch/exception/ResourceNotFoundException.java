package pl.ur8an5ky.livematch.exception;

/**
 * Thrown when a resource (team, match, event) with the given ID does not exist.
 * The global handler translates it into HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super("%s with ID %d was not found".formatted(resourceName, id));
    }
}