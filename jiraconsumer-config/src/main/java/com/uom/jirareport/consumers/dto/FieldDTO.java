package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class FieldDTO implements Serializable {

    @JsonProperty
    private IssueTypeDTO issueType;
}
