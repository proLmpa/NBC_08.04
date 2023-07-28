package com.sparta.dtogram.common.exception;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DtogramException extends RuntimeException{
    private final DtogramErrorCode errorCode;

    public DtogramException(DtogramErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause, false, false);
        this.errorCode = errorCode;
    }
}
