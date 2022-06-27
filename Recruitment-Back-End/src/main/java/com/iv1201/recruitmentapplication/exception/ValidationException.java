package com.iv1201.recruitmentapplication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * result returned from <code>@Validator interface</code>
 */
public class ValidationException extends RuntimeException {
    private String msg;
    private final Map<String, Field> fields = new HashMap<>();
    private boolean error;

    public ValidationException() {

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setError(boolean err) {
        this.error = err;
    }

    public boolean hasError() {
        return error;
    }

    public void addFieldError(String fieldName, Object fieldValue, String... msg) {
        Field field = new Field(fieldName, fieldValue, Arrays.asList(msg));
        fields.put(fieldName, field);
    }

    public Collection<Field> errors() {
        return this.fields.values();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class Field {
        private String fieldName;
        private Object fieldValue;
        private List<String> errors = new ArrayList<>();
    }

}