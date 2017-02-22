package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 22/02/2017.
 */
@Getter
public class ProjectDTO implements Serializable {

    private String projectKey;
    private String projectName;

    private ProjectDTO(ProjectBuilder builder) {
        this.projectKey = builder.projectKey;
        this.projectName = builder.projectName;
    }

    public static class ProjectBuilder {
        private final String projectKey;
        private final String projectName;

        public ProjectBuilder(String projectKey, String projectName) {
            this.projectKey = projectKey;
            this.projectName = projectName;
        }

        public ProjectDTO build() {
            return new ProjectDTO(this);
        }

    }
}
