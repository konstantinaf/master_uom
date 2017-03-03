package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fotarik on 01/03/2017.
 */
@Getter
public class IssueDTO  implements Serializable {

    @JsonProperty
    private String expand;
    @JsonProperty
    private String id;
    @JsonProperty
    private String self;
    @JsonProperty
    private String key;
    @JsonProperty
    public FieldDTO fields;
    private Map<String, String> components;
    @JsonProperty("timespent")
    private String timeSpent;
    @JsonProperty("timeoriginalestimate")
    private String timeOriginalEstimate;
    @JsonProperty
    private String description;
    private Map<String, String> fixVersions;
    @JsonProperty
    private String creator;
    @JsonProperty
    private String displayName;
    private List<String> subtasks;

    @JsonAnyGetter
    public Map<String, String> getComponents() {
        return components;
    }

    @JsonAnySetter
    public void setComponents(Map<String, String> components) {
        this.components = components;
    }

    @JsonAnyGetter
    public Map<String, String> getFixVersions() {
        return fixVersions;
    }

    @JsonAnySetter
    public void setFixVersions(Map<String, String> fixVersions) {
        this.fixVersions = fixVersions;
    }

    @JsonAnyGetter
    public List<String> getSubtasks() {
        return subtasks;
    }

    @JsonAnySetter
    public void setSubtasks(List<String> subtasks) {
        this.subtasks = subtasks;
    }
}
