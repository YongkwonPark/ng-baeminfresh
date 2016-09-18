package com.woowahan.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author ykpark@woowahan.com
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class BusinessException extends SystemException {

    static final String DEFAULT_MESSAGE = "서버가 요청 처리하는 중 내부 오류가 발생했다.";

    public BusinessException() {
        this(DEFAULT_MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }

    @Override
    public String[] getCodes() {
        return new String[] { String.format("error.business.%s", getClass().getSimpleName()) };
    }

    @Override
    public String getDefaultMessage() {
        return getMessage();
    }

}
