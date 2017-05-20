package com.uom.jirareport.controller;

import com.uom.jirareport.consumers.dto.DataBugsReportDTO;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.services.JiraHttpRequestService;
import com.uom.jirareport.consumers.dto.PieReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fotarik on 13/02/2017.
 */
@Controller
public class JiraReportController {

    @Autowired
    JiraHttpRequestService jiraHttpRequestService;

    @RequestMapping(value="/projects", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ProjectDTO> getJiraProjects(HttpServletRequest request) throws Exception {

        String jiraBaseUrl = request.getParameter("url");
        List<ProjectDTO> projectList = jiraHttpRequestService.getDomainProjectFromJira(jiraBaseUrl);

        return projectList;
    }


    @RequestMapping(value="/monthlybugs", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public DataBugsReportDTO getBugsCountPerMonth(HttpServletRequest request) throws Exception {

        String jiraBaseUrl = request.getParameter("url");
        String projectKey = request.getParameter("projectKey");

        DataBugsReportDTO dataDTO = jiraHttpRequestService.getMonthlyBugsReport(jiraBaseUrl, projectKey);

        return dataDTO;
    }

    @RequestMapping(value="/assigneebugs", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public DataBugsReportDTO getBugsCountPerDevPerMonth(HttpServletRequest request) throws Exception {

        String jiraBaseUrl = request.getParameter("url");
        String projectKey = request.getParameter("projectKey");

        DataBugsReportDTO dataDTO = jiraHttpRequestService.getAssigneeBugsReport(jiraBaseUrl, projectKey);

        return dataDTO;
    }

    @RequestMapping(value="/versionbugs", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public PieReportDTO getVersionBugsReport(HttpServletRequest request) throws Exception {

        String jiraBaseUrl = request.getParameter("url");
        String projectKey = request.getParameter("projectKey");

        PieReportDTO dataDTO = jiraHttpRequestService.getVersionBugsReport(jiraBaseUrl, projectKey);

        return dataDTO;
    }
}
