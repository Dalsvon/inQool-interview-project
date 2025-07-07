package cz.svonavec.tennis.exception;

/**
 * Exception thrown when user sends unusable data and application needs to return code 400
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
