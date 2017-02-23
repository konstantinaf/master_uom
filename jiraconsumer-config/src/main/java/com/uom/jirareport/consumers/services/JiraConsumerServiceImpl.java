package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.internal.json.ProjectJsonParser;
import com.uom.jirareport.consumers.dao.JiraConsumerRepository;
import com.uom.jirareport.consumers.dto.JiraConsumer;
import com.uom.jirareport.consumers.dto.ProjectDTO;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.oauth.Command;
import com.uom.jirareport.consumers.oauth.JiraOAuthClient;
import com.uom.jirareport.consumers.oauth.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by fotarik on 13/02/2017.
 */
@Slf4j
@Service
public class JiraConsumerServiceImpl implements JiraConsumerService {

    private String accessToken = "";
    final static List<String> commands = new ArrayList<>();
    private Optional<JiraConsumer> jiraConsumer;
    private OAuthClient oAuthClient;

    private ProjectJsonParser projectJsonParser = new ProjectJsonParser();

    static {
        commands.add("requestToken");
        commands.add("accessToken");
        commands.add("request");
    }

    @Autowired
    JiraConsumerRepository jiraConsumerRepository;

    @Override
    public ServiceResponse getAuthorizationUrl(String domainName) throws Exception {
        jiraConsumer = Optional.ofNullable(jiraConsumerRepository.findJiraConsumerByHomeUrl(domainName));

        List<String> argumentsWithoutFirst = commands.subList(1, commands.size());
        ServiceResponse.ServiceResponseBuilder builder;
        try {
            if (jiraConsumer.isPresent()) {
                oAuthClient = new OAuthClient(new JiraOAuthClient(jiraConsumer.get().getJiraConsumerKey().getJiraUrl()), jiraConsumer.get());
                oAuthClient.execute(Command.fromString(commands.get(0)), argumentsWithoutFirst);

                builder = new ServiceResponse.ServiceResponseBuilder(oAuthClient.getAuthorizationUrl(), null, null);

            } else {
                builder = new ServiceResponse.ServiceResponseBuilder(null, 12, "Error");
            }
        } catch (Exception e) {
            log.error("Cannot create OAuthClient ", e);
            throw e;
        }

        return builder.build();

    }

    private void getAccessToken(String oauthToken, String oauthVerifier) throws Exception {
        List<String> argumentsForAccessToken = new ArrayList<>();
        argumentsForAccessToken.add(oauthToken);
        argumentsForAccessToken.add(oauthVerifier);

        try {
            if (jiraConsumer.isPresent()) {
                oAuthClient.execute(Command.fromString(commands.get(1)), argumentsForAccessToken);
                accessToken = oAuthClient.getAccessToken();
            }
        } catch (Exception e) {
            log.error("Cannot create OAuthClient ", e);
            throw e;
        }

    }

    @Override
    public List<ProjectDTO> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception {
        this.getAccessToken(oauthToken, oauthVerifier);

        String jqlQuery = "project";
        List<String> argumentsForRequest = new ArrayList<>();
        argumentsForRequest.add(jiraConsumer.get().getJiraRestUrl() + jqlQuery);
        argumentsForRequest.add(oauthVerifier);
        argumentsForRequest.add(accessToken);

        oAuthClient.execute(Command.fromString(commands.get(2)), argumentsForRequest);

        List<ProjectDTO> projectList = new ArrayList<>();

        JSONArray jsonarray = new JSONArray(oAuthClient.getHttpResponse().parseAsString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);

            ObjectMapper mapper = new ObjectMapper();
            ProjectDTO projectDTO = mapper.readValue(jsonobject.toString(), ProjectDTO.class);

            projectList.add(projectDTO);
        }

        return projectList;
    }


}
