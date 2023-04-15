package com.atguigu.system.handler;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.system.execption.GuiguException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }

    /**
     * 特定异常
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }

    @ExceptionHandler(GuiguException.class)
    public Result error(GuiguException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }

}
