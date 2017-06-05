package com.uom.jirareport.consumers.services;

import com.uom.jirareport.consumers.dto.DataBugsReportDTO;
import com.uom.jirareport.consumers.dto.PieReportDTO;
import com.uom.jirareport.consumers.dto.ProjectDTO;

import java.util.List;

/**
 * Created by fotarik on 17/05/2017.
 */
public interface JiraHttpRequestService {

    List<ProjectDTO> getDomainProjectFromJira(String jiraBaseUrl);

    DataBugsReportDTO getMonthlyBugsReport(String jiraBaseUrl, String projectKey);

    DataBugsReportDTO getAssigneeBugsReport(String jiraBaseUrl, String projectKey);

    PieReportDTO getVersionBugsReport(String jiraBaseUrl, String projectKey);

    PieReportDTO getCreatedResolvedBugs(String jiraBaseUrl, String projectKey);
}
