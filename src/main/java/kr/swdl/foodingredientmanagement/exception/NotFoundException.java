package kr.swdl.foodingredientmanagement.exception;

public class NotFoundException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "Fail found";
    public NotFoundException(ErrorDomain domain) {
        this(domain, DEFAULT_MESSAGE);
    }
    public NotFoundException(ErrorDomain domain, String message) {
        super(domain, message);
    }
    public NotFoundException(ErrorDomain domain, String message, Throwable cause) {
        super(domain, message, cause);
    }
}
