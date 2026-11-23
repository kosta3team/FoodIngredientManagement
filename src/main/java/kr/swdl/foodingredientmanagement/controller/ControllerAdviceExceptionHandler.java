package kr.swdl.foodingredientmanagement.controller;

import kr.swdl.foodingredientmanagement.exception.BusinessException;
import kr.swdl.foodingredientmanagement.exception.CreateFailedException;
import kr.swdl.foodingredientmanagement.exception.NotFoundException;
import kr.swdl.foodingredientmanagement.exception.UpdateFailedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerAdviceExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException e) {
        ModelAndView mav = new ModelAndView("error/business-error");
        mav.addObject("message", mappingExceptionToHttpStatus(e));
        return mav;
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
