package com.sparta.dtogram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DtogramErrorCode {
    INVALID_TYPE_VALUE(HttpStatus.SC_BAD_REQUEST, "입력이 규정에 어긋나거나 일부 요소가 공백입니다."),
    IN_USED_USERNAME(HttpStatus.SC_BAD_REQUEST, "중복된 username입니다."),
    IN_USED_NICKNAME(HttpStatus.SC_BAD_REQUEST, "중복된 nickname입니다."),
    IN_USED_EMAIL(HttpStatus.SC_BAD_REQUEST, "중복된 email입니다."),
    WRONG_ADMIN_TOKEN(HttpStatus.SC_BAD_REQUEST, "관리자 암호가 틀려 등록이 불가능합니다."),
    WRONG_PASSWORD(HttpStatus.SC_BAD_REQUEST, "비밀번호가 틀렸습니다"),
    USER_NOT_FOUND(400, "해당 사용자는 존재하지 않습니다."),
    POST_NOT_FOUND(400, "해당 게시글은 존재하지 않습니다."),
    REPLY_NOT_FOUND(400, "해당 댓글은 존재하지 않습니다."),
    TAG_ALREADY_EXISTS(400, "해당 태그는 이미 존재합니다."),
    TAG_NOT_FOUND(400, "해당 태그는 존재하지 않습니다."),
    UNAUTHORIZED_USER(400, "작성자만 수정/삭제할 수 있습니다.");

    private final int errorCode;
    private final String errorMessage;
}