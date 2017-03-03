package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class IssueResponseDTO implements Serializable {

    @JsonProperty
    private String expand;
    @JsonProperty
    private String startAt;
    @JsonProperty
    private int maxResults;
    @JsonProperty
    private int total;
    @JsonProperty
    private IssueDTO[] issues;
}
