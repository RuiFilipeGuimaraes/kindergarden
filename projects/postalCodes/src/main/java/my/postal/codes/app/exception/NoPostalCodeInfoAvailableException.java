package my.postal.codes.app.exception;

public class NoPostalCodeInfoAvailableException extends RuntimeException {

    public NoPostalCodeInfoAvailableException(String message) {
        super(message);
    }
}
