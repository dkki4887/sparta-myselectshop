package com.sparta.myselectshop.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//ControllerAdvice에 ResponseBody가 붙은 것
//클래스 단위의 어노테이션 -> 클래스에만 달 수 잇당
/*
RestControllerAdvice 사용 이유

1. 예외처리의 중앙 집중화
    -> 이 클래스 하나로 예외처리를 모두 컨트롤 가능. 모든 컨트롤러 쫓아다니면서 수정 안해도 됨
    -> 코드 중복 방지 및 유지 보수에도 굿

2. 예외 처리 로직을 모듈화해서 관리하기 용이
 */
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }
}
