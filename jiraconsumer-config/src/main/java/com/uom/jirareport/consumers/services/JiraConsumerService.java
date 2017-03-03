package com.uom.jirareport.consumers.services;

import com.uom.jirareport.consumers.dto.IssueDTO;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;

import java.io.IOException;
import java.util.List;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface JiraConsumerService {

    ServiceResponse getAuthorizationUrl(String url) throws Exception;

    List<ProjectDTO> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception;

    List<IssueDTO> getIssuesByProjectKey(String projectKey, String oauthVerifier) throws Exception;
}
