package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.exception.BusinessException;
import kr.swdl.foodingredientmanagement.exception.CreateFailedException;
import kr.swdl.foodingredientmanagement.exception.NotFoundException;
import kr.swdl.foodingredientmanagement.exception.UpdateFailedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class RestControllerAdviceExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(mappingExceptionToHttpStatus(e)).body(e.getClass().toString());
    }

    private HttpStatus mappingExceptionToHttpStatus(BusinessException e) {
        if (e instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        }
        if (e instanceof CreateFailedException
        || e instanceof UpdateFailedException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR; // 분리되지 않은 예외
    }
}
