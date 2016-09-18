package com.woowahan.common;

/**
 * @author ykpark@woowahan.com
 */
public abstract class ResourceNotFoundException extends SystemException {

    static final String DEFAULT_MESSAGE = "서버가 요청한 자원을 찾을 수 없다.";

    public ResourceNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public String[] getCodes() {
        return new String[] { String.format("error.system.%s", getClass().getSimpleName()) };
    }

    @Override
    public String getDefaultMessage() {
        return getMessage();
    }

}
