package com.iv1201.recruitmentapplication.model.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorBody {
    private String message;
    private Object errors;

    public ApiErrorBody(String message, Object body) {
        super();
        this.message = message;
        this.errors = body;
    }
}