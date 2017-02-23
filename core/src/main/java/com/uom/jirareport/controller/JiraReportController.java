package com.uom.jirareport.controller;

import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.services.JiraConsumerServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
        System.out.println("Oauth token " + request.getParameter("oauthToken"));
        System.out.println("Oauth verifier " + request.getParameter("oauthVerifier"));

        amadeusCityService.getDomainProjectsFromJira(request.getParameter("oauthToken"),request.getParameter("oauthVerifier") );
        ProjectDTO.ProjectBuilder builder = new ProjectDTO.ProjectBuilder("COOK", "Cook Project");

        List<ProjectDTO> projectDTOs = new ArrayList<>();
        projectDTOs.add(builder.build());
        return projectDTOs;
    }
}
