package com.iv1201.recruitmentapplication.model.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iv1201.recruitmentapplication.entity.ApplicationState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationStateUpdateRequest {
    @JsonIgnore
    private long applicationId;
    private final ApplicationState.State newState;
    private final long version;

    public ApplicationStateUpdateRequest(ApplicationState.State newState, long version) {
        this.newState = newState;
        this.version = version;
    }


}
