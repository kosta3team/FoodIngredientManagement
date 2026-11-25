package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.exception.BusinessException;
import kr.swdl.foodingredientmanagement.exception.CreateFailedException;
import kr.swdl.foodingredientmanagement.exception.NotFoundException;
import kr.swdl.foodingredientmanagement.exception.UpdateFailedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControllerAdviceExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException e) {
        HttpStatus status = mappingExceptionToHttpStatus(e);
        ModelAndView mav = new ModelAndView("error/error");
        mav.setStatus(status);
        mav.addObject("status", status.value());
        mav.addObject("error", status.getReasonPhrase());
        mav.addObject("message", e.getMessage());
        return mav;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleBusinessException(NoSuchElementException e) {
        return ResponseEntity.status(mappingExceptionToHttpStatus(e)).body(e.getClass().toString());
    }

    private HttpStatus mappingExceptionToHttpStatus(RuntimeException e) {
        if (e instanceof NotFoundException
                || e instanceof NoSuchElementException) {
            return HttpStatus.NOT_FOUND;
        }
        if (e instanceof CreateFailedException
                || e instanceof UpdateFailedException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR; // 분리되지 않은 예외
    }
}
