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
     * This method gets the domain projects from the jira url
     * @return List<ProjectDTO>
     * @throws Exception
     */
    List<ProjectDTO> getDomainProjectsFromJira() throws Exception;

    /**
     * This method gets all the issues by project with jql
     * @param projectKey
     * @return a list of issues
     * @throws Exception
     */
//    List<Issue> getIssuesByProjectKey(String projectKey) throws Exception;
//
//    /**
//     * This method prepares all the data for one chart bugs per month
//     * @param projectKey
//     * @return
//     * @throws Exception
//     */
//    DataBugsReportDTO getBugsCountPerMonth(String projectKey) throws Exception;
//
//    /**
//     * This method prepares all the data for one chart bugs per month
//     * @param projectKey
//     * @return
//     * @throws Exception
//     */
//    DataBugsReportDTO getBugsCountPerAssignee(String projectKey) throws Exception;
}
