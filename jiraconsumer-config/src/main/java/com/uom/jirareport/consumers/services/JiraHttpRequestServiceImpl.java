package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import com.uom.jira.consumers.utils.HttpRequestUtils;
import com.uom.jira.consumers.utils.JSONUtils;
import com.uom.jira.consumers.utils.ReportUtils;
import com.uom.jirareport.consumers.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fotarik on 17/05/2017.
 */
@Slf4j
@Service
public class JiraHttpRequestServiceImpl implements JiraHttpRequestService {

    private static final String JIRA_REST_API = "/rest/api/latest/";
    private static final String PROJECT_QUERY ="project";
    private static final String SEARCH_JQL = "search?jql=";
    private final static String JQL_ISSUES_BY_PROJECT = "project = '";
    private final static String FIELDS_TO_SHOW = "&fields=id,key";
    private final static String JQL_TYPE_BUG = " AND issuetype = bug ";
    private final static String ISSUE_QUERY = "issue/";
    private final static String JQL_EXPAND = "?expand=names,schema";


    @Override
    public List<ProjectDTO> getDomainProjectFromJira(String jiraBaseUrl) {
        List<ProjectDTO> projectDTOS = null;
        String response;
        try {
            response = HttpRequestUtils.executeHttpRequest(jiraBaseUrl+JIRA_REST_API+PROJECT_QUERY);
            if (response != null) {
                projectDTOS = JSONUtils.getJSONArrayFromResponse(response.toString(), ProjectDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectDTOS;
    }

    @Override
    public DataBugsReportDTO getMonthlyBugsReport(String jiraBaseUrl, String projectKey) {

        List<Issue> bugs = this.executeReportRequest(jiraBaseUrl, projectKey);

        Map<Integer, List<Issue>> bugsPerYear = ReportUtils.countBugsPerYear(bugs);

        Map<Integer, Map<Integer, Double>> bugsPerMonthPerYear = ReportUtils.countBugsPerMonthPerYear(bugsPerYear);

        return ReportUtils.buildDataResponseForChart(bugsPerMonthPerYear);
    }

    @Override
    public DataBugsReportDTO getAssigneeBugsReport(String jiraBaseUrl, String projectKey) {
        List<Issue> bugs = this.executeReportRequest(jiraBaseUrl, projectKey);
        Map<String, Map<Integer, Double>> bugsPerAssigneePerMonth;
        Map<String, Map<Integer, Double>> bugsPerAssigneePerMonthWithGini;

        ReportUtils.excludeBugsWithoutAssignee(bugs);

        bugsPerAssigneePerMonth = ReportUtils.countBugsPerAssigneePerMonth(bugs);

        bugsPerAssigneePerMonthWithGini = ReportUtils.prepareDataForGiniRatio(bugsPerAssigneePerMonth);

        return ReportUtils.buildResponseForAssigneeBugs(bugsPerAssigneePerMonthWithGini);
    }

    @Override
    public PieReportDTO getVersionBugsReport(String jiraBaseUrl, String projectKey) {
        List<Issue> bugs = this.executeReportRequest(jiraBaseUrl, projectKey);

        ReportUtils.excludeBugsWithoutAffectedVersion(bugs);

        List<BugVersion> bugVersions = ReportUtils.createListOfBugVersion(bugs);

        Map<String, Double> bugsPerVersion = ReportUtils.countBugsPerAffectedVersion(bugVersions);

        return ReportUtils.buildDataResponseForPieChart(bugsPerVersion);
    }

    @Override
    public PieReportDTO getCreatedResolvedBugs(String jiraBaseUrl, String projectKey) {
        List<Issue> bugs = this.executeReportRequest(jiraBaseUrl, projectKey);

        Map<String, Double> resolvedStatus = ReportUtils.countBugsByResolvedStatus(bugs);

        return ReportUtils.buildDataResponseForPieChart(resolvedStatus);

    }

    private List<Issue> executeReportRequest(String jiraBaseUrl, String projectKey) {
        List<Issue> bugs = new ArrayList<>();

        String response;
        try {

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(jiraBaseUrl);
            urlBuilder.append(JIRA_REST_API);
            urlBuilder.append(SEARCH_JQL);
            urlBuilder.append(URLEncoder.encode(JQL_ISSUES_BY_PROJECT, "UTF-8"));
            urlBuilder.append(projectKey);
            urlBuilder.append(URLEncoder.encode("'") + URLEncoder.encode( JQL_TYPE_BUG));
            urlBuilder.append(FIELDS_TO_SHOW);

            response = HttpRequestUtils.executeHttpRequest(urlBuilder.toString());

            if (response != null) {

                JSONObject jsonObject = JSONUtils.getJSONObjectFromResponse(response.toString());

                IssueResponseDTO  issueResponseDTO = JSONUtils.mapJSONObjectToDTO(jsonObject, IssueResponseDTO.class);

                IssueJsonParser issueJsonParser = new IssueJsonParser();

                for (IssueDTO issueDTO : issueResponseDTO.getIssues()) {

                    response = HttpRequestUtils.executeHttpRequest(jiraBaseUrl + JIRA_REST_API + ISSUE_QUERY + issueDTO.getKey() + JQL_EXPAND);

                    jsonObject = JSONUtils.getJSONObjectFromResponse(response.toString());

                    Issue issue = issueJsonParser.parse(jsonObject);

                    bugs.add(issue);
                }
            }
        } catch (Exception e) {
            log.error("Cannot get the issues for project " + projectKey, e);
        }

        return bugs;
    }


}