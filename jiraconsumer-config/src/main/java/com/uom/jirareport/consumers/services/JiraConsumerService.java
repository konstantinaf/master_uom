package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Project;
import com.uom.jirareport.consumers.dto.ServiceResponse;

import java.util.List;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface JiraConsumerService {

    ServiceResponse getAuthorizationUrl(String url) throws Exception;

    List<Project> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception;
}
