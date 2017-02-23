package com.uom.jirareport.controller;

import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.services.JiraConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by fotarik on 13/02/2017.
 */
@Controller
public class JiraReportController {

    @Autowired
    JiraConsumerServiceImpl amadeusCityService;

    @RequestMapping(value="/oauth", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ServiceResponse authorizeUser(HttpServletRequest request) throws Exception {
        ServiceResponse serviceResponse = null;
            Optional<String> url = Optional.ofNullable(request.getParameter("url"));
            if (url.isPresent()) {
                serviceResponse = amadeusCityService.getAuthorizationUrl(url.get());
            }
        return serviceResponse;
    }

    @RequestMapping(value="/projects", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ProjectDTO> getJiraProjects(HttpServletRequest request) throws Exception {

        String oauthToken = request.getParameter("oauthToken");
        String oauthVerifier = request.getParameter("oauthVerifier");
        //todo error handling
        List<ProjectDTO> projectList = amadeusCityService.getDomainProjectsFromJira(oauthToken, oauthVerifier);

        return projectList;
    }
}
