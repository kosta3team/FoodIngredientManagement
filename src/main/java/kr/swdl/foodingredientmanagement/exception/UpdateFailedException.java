package kr.swdl.foodingredientmanagement.exception;

public class UpdateFailedException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "Fail update";
    public UpdateFailedException(ErrorDomain domain) {
        this(domain, DEFAULT_MESSAGE);
    }
    public UpdateFailedException(ErrorDomain domain, String message) {
        super(domain, message);
    }
    public UpdateFailedException(ErrorDomain domain, String message, Throwable cause) {
        super(domain, message, cause);
    }
}
