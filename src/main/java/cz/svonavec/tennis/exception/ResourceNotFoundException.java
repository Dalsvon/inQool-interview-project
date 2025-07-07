package cz.svonavec.tennis.exception;

/**
 * Exception thrown when resource user wants to change cannot be found and application needs to return code 404
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
