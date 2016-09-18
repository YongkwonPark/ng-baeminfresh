package com.woowahan.common;

import org.springframework.context.MessageSourceResolvable;

/**
 * @author ykpark@woowahan.com
 */
public abstract class SystemException extends RuntimeException implements MessageSourceResolvable {

    static final String DEFAULT_MESSAGE = "시스템 오류가 발생했습니다. (지속적으로 발생시 관리자에게 연락하세요)";


    protected SystemException() {
        this(DEFAULT_MESSAGE);
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public SystemException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        this(DEFAULT_MESSAGE, cause, enableSuppression, writableStackTrace);
    }

    public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public String[] getCodes() {
        return new String[] { "error.system" };
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

}
