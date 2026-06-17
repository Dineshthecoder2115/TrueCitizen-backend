package com.project.FixMyStreet.Exception;


import com.project.FixMyStreet.DTO.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(
            RuntimeException ex) {

        return new ApiResponse(
                false,
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(
            Exception ex) {

        return new ApiResponse(
                false,
                ex.getMessage()
        );
    }
}