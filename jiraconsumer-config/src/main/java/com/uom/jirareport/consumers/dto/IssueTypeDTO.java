package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class IssueTypeDTO implements Serializable {

    @JsonProperty
    private String self;
    @JsonProperty
    private String id;
    @JsonProperty
    private String description;
    @JsonProperty
    private String iconUrl;
    @JsonProperty
    private String name;
    @JsonProperty
    private boolean subtask;
}
