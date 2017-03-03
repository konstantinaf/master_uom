package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface JiraConsumerService {

    ServiceResponse getAuthorizationUrl(String url) throws Exception;

    List<ProjectDTO> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception;

    List<Issue> getIssuesByProjectKey(String projectKey, String oauthVerifier) throws Exception;

    Map<Integer, Long> getBugsCountPerMonth(String projectKey, String oauthVerifier) throws Exception;
}
