package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * This class represents the filter variables that can be used
 * to filter through applications by the respective properties.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    @JsonProperty("state")
    private String competence;
    @JsonProperty("competences")
    private String name;
    @JsonProperty("page")
    private int page;
    @JsonProperty("size")
    private int size;
}
