package com.iv1201.recruitmentapplication.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionPayload<T> {
    private final T payload;
}