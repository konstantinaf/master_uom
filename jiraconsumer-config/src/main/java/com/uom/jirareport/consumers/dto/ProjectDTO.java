package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by fotarik on 22/02/2017.
 */
@Getter
public class ProjectDTO implements Serializable {

    @JsonProperty
    private String expand;
    @JsonProperty
    private String self;
    @JsonProperty
    private String id;
    @JsonProperty
    private String key;
    @JsonProperty
    private String name;
    private Map<String,String> avatarUrls;
    @JsonProperty
    private String projectTypeKey;

    @JsonAnyGetter
    public Map<String, String> getAvatarUrls() {
        return avatarUrls;
    }

    @JsonAnySetter
    public void setAvatarUrls(Map<String, String> avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

}
