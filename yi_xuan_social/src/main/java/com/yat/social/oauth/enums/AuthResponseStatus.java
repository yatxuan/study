package com.yat.social.oauth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 通用的状态码对照表 </p>
 * @author Yat-Xuan
 * @date 2020/4/8 17:34
*/
@Getter
@AllArgsConstructor
public enum AuthResponseStatus {
    /**
     * 2000：正常；
     * other：调用异常，具体异常内容见{@code msg}
     */
    SUCCESS(2000, "Success"),
    FAILURE(5000, "Failure"),
    NOT_IMPLEMENTED(5001, "Not Implemented"),
    PARAMETER_INCOMPLETE(5002, "Parameter incomplete"),
    UNSUPPORTED(5003, "Unsupported operation"),
    NO_AUTH_SOURCE(5004, "AuthDefaultSource cannot be null"),
    UNIDENTIFIED_PLATFORM(5005, "Unidentified platform"),
    ILLEGAL_REDIRECT_URI(5006, "Illegal redirect uri"),
    ILLEGAL_REQUEST(5007, "Illegal request"),
    ILLEGAL_CODE(5008, "Illegal code"),
    ILLEGAL_STATUS(5009, "Illegal state"),
    REQUIRED_REFRESH_TOKEN(5010, "The refresh token is required; it must not be null"),
    ;

    private int code;
    private String msg;
}

