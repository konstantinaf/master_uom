package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDTO implements Serializable {

    @JsonProperty
    private int id;
    @JsonProperty
    private String self;
    @JsonProperty
    private String key;

}
