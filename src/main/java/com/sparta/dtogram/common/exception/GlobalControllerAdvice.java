package com.sparta.dtogram.common.exception;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({DtogramException.class})
    public ResponseEntity<ApiResponseDto> handlerDtogramException(DtogramException e){
        ApiResponseDto restApiException = new ApiResponseDto(e.getMessage(), HttpStatus.SC_BAD_REQUEST);
        return ResponseEntity.badRequest().body(restApiException);
    }
}
