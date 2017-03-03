package com.uom.jirareport.controller;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.services.JiraConsumerService;
import com.uom.jirareport.consumers.services.JiraConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by fotarik on 13/02/2017.
 */
@Controller
public class JiraReportController {

    @Autowired
    JiraConsumerService jiraConsumerService;

    @RequestMapping(value="/oauth", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ServiceResponse authorizeUser(HttpServletRequest request) throws Exception {
        ServiceResponse serviceResponse = null;
            Optional<String> url = Optional.ofNullable(request.getParameter("url"));
            if (url.isPresent()) {
                serviceResponse = jiraConsumerService.getAuthorizationUrl(url.get());
            }
        return serviceResponse;
    }

    @RequestMapping(value="/projects", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ProjectDTO> getJiraProjects(HttpServletRequest request) throws Exception {

        String oauthToken = request.getParameter("oauthToken");
        String oauthVerifier = request.getParameter("oauthVerifier");
        //todo error handling
        List<ProjectDTO> projectList = jiraConsumerService.getDomainProjectsFromJira(oauthToken, oauthVerifier);

        return projectList;
    }

    @RequestMapping(value="/todo change", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Issue> getJiraProjectIssues(HttpServletRequest request) throws Exception {

        String projectKey = request.getParameter("projectKey");
        String oauthVerifier = request.getParameter("oauthVerifier");
        //todo error handling
        List<Issue> issuesList = jiraConsumerService.getIssuesByProjectKey(projectKey, oauthVerifier);

        return issuesList;
    }

    @RequestMapping(value="/issues", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<Integer, Long> getBugsCountPerMonth(HttpServletRequest request) throws Exception {

        String projectKey = request.getParameter("projectKey");
        String oauthVerifier = request.getParameter("oauthVerifier");
        //todo error handling
        Map<Integer, Long> bugsCountPerMonth = jiraConsumerService.getBugsCountPerMonth(projectKey, oauthVerifier);

        return bugsCountPerMonth;
    }
}
