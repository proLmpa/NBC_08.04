package com.sparta.dtogram.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString // 출력 시 각 field명을 포함시키는 annotation
public class ApiResponseDto {
    private int statusCode;
    private String statusMessage;

    @Builder // 해당 annotation이 적힌 field는 반드시 초기화가 되어있어야 한다.
    public ApiResponseDto(String statusMessage, int statusCode){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
