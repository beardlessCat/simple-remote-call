package com.bigyj.exception;

import com.bigyj.common.entity.ApiResult;
import com.bigyj.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ApiException.class)
    public ApiResult<Object> apiExceptionHandler(ApiException e) {
        return new ApiResult<>(e.getCode(), null, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Object> defaultExceptionHandler(Exception e) {
        logger.error("未知异常", e);
        return new ApiResult<>("99999", null, "未知异常");

    }
}
