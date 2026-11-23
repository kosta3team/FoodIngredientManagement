package kr.swdl.foodingredientmanagement.exception;

public class CreateFailedException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "Fail create";
    public CreateFailedException(ErrorDomain domain) {
        this(domain, DEFAULT_MESSAGE);
    }
    public CreateFailedException(ErrorDomain domain, String message) {
        super(domain, message);
    }
    public CreateFailedException(ErrorDomain domain, String message, Throwable cause) {
        super(domain, message, cause);
    }
}
