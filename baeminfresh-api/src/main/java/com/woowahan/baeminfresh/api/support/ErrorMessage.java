package com.woowahan.baeminfresh.api.support;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Objects;

/**
 * @author ykpark@woowahan.com
 */
public class ErrorMessage {

    private final HttpStatus status;
    private final String error;
    private final String message;
    private final Date timestamp = new Date();

    private String path;
    private Object additionalInformation;


    public ErrorMessage(HttpStatus status) {
        this(status, "No message available");
    }

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public ErrorMessage(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }


    public ErrorMessage path(String value) {
        if (Objects.nonNull(value)) {
            this.path = value;
        }

        return this;
    }

    public ErrorMessage additionalInformation(Object value) {
        if (Objects.nonNull(value)) {
            this.additionalInformation = value;
        }

        return this;
    }


    public String getPath() {
        return path;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Object getAdditionalInformation() {
        return additionalInformation;
    }

    public Date getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "ErrorMessage { path='" + path + "\', status=" + status +
                ", error='" + error + "\', message='" + message + "\' }";
    }

}
