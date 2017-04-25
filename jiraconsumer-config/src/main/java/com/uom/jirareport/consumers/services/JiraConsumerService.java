package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.uom.jirareport.consumers.dto.DataBugsReportDTO;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;

import java.util.List;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface JiraConsumerService {

    /**
     * This method uses oAuth to authorize the user
     * @param url
     * @return ServiceResponse
     * @throws Exception
     */
    ServiceResponse getAuthorizationUrl(String url) throws Exception;

    /**
     * This method gets the domain projects from the jira url
     * @param oauthToken
     * @param oauthVerifier
     * @return List<ProjectDTO>
     * @throws Exception
     */
    List<ProjectDTO> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception;

    /**
     * This method gets all the issues by project with jql
     * @param projectKey
     * @param oauthVerifier
     * @return a list of issues
     * @throws Exception
     */
    List<Issue> getIssuesByProjectKey(String projectKey, String oauthVerifier) throws Exception;

    /**
     * This method prepares all the data for one chart bugs per month
     * @param projectKey
     * @param oauthVerifier
     * @return
     * @throws Exception
     */
    DataBugsReportDTO getBugsCountPerMonth(String projectKey, String oauthVerifier) throws Exception;

    /**
     * This method prepares all the data for one chart bugs per month
     * @param projectKey
     * @param oauthVerifier
     * @return
     * @throws Exception
     */
    DataBugsReportDTO getBugsCountPerAssignee(String projectKey, String oauthVerifier) throws Exception;
}
