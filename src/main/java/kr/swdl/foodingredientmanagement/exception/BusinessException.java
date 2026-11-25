package kr.swdl.foodingredientmanagement.exception;


public abstract class BusinessException extends RuntimeException {
    protected BusinessException(ErrorDomain domain, String message) {
        super(domain + " : " + message);
    }
    protected BusinessException(ErrorDomain domain, String message, Throwable cause) {
        super(domain + " : " + message, cause);
    }
}
