package com.sparta.dtogram.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgResponseDto {
    private String message;
    private Integer statusCode;

    public MsgResponseDto(String massage, Integer statusCode) {
        this.message = massage;
        this.statusCode = statusCode;
    }
}
