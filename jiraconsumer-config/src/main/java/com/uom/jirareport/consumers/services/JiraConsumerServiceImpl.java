package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import com.uom.jirareport.consumers.dao.JiraConsumerRepository;
import com.uom.jirareport.consumers.dto.*;
import com.uom.jirareport.consumers.oauth.Command;
import com.uom.jirareport.consumers.oauth.JiraOAuthClient;
import com.uom.jirareport.consumers.oauth.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<Issue> getIssuesByProjectKey(String projectKey, String oauthVerifier) throws Exception {

        List<Issue> issues = new ArrayList<>();
        String jqlQuery = "search?jql=project" + ("%20%3D%20" + projectKey + "&fields=id,key");

        List<String> argumentsForRequest = new ArrayList<>();
        argumentsForRequest.add(jiraConsumer.get().getJiraRestUrl() + jqlQuery);
        argumentsForRequest.add(oauthVerifier);
        argumentsForRequest.add(accessToken);

        oAuthClient.execute(Command.fromString(commands.get(2)), argumentsForRequest);

        JSONObject jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

        ObjectMapper mapper = new ObjectMapper();
        IssueResponseDTO issueResponseDTO = mapper.readValue(jsonObject.toString(), IssueResponseDTO.class);


        IssueJsonParser issueJsonParser = new IssueJsonParser();

        for (IssueDTO issueDTO : issueResponseDTO.getIssues()) {
            jqlQuery = "issue/"+issueDTO.getKey()+"?expand=names,schema";
            argumentsForRequest = new ArrayList<>();
            argumentsForRequest.add(jiraConsumer.get().getJiraRestUrl() + jqlQuery);
            argumentsForRequest.add(oauthVerifier);
            argumentsForRequest.add(accessToken);

            oAuthClient.execute(Command.fromString(commands.get(2)), argumentsForRequest);

            jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

            Issue issue = issueJsonParser.parse(jsonObject);

            issues.add(issue);
        }

        System.out.println("******************************* " +issues.size());
        return  issues;
    }

    @Override
    //todo Per YEAR!
    public Map<Integer, Long> getBugsCountPerMonth(String projectKey, String oauthVerifier) throws Exception {
        Map<Integer, Long> bugsPerMonth = new HashMap<>();
        List<Issue> bugs = new ArrayList<>();
        String jqlQuery = "search?jql=project" + ("%20%3D%20" + projectKey + "%20AND%20issuetype%20%3D%20bug&fields=id,key");

        List<String> argumentsForRequest = new ArrayList<>();
        argumentsForRequest.add(jiraConsumer.get().getJiraRestUrl() + jqlQuery);
        argumentsForRequest.add(oauthVerifier);
        argumentsForRequest.add(accessToken);

        oAuthClient.execute(Command.fromString(commands.get(2)), argumentsForRequest);

        JSONObject jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

        ObjectMapper mapper = new ObjectMapper();
        IssueResponseDTO issueResponseDTO = mapper.readValue(jsonObject.toString(), IssueResponseDTO.class);


        IssueJsonParser issueJsonParser = new IssueJsonParser();

        for (IssueDTO issueDTO : issueResponseDTO.getIssues()) {
            jqlQuery = "issue/"+issueDTO.getKey()+"?expand=names,schema";
            argumentsForRequest = new ArrayList<>();
            argumentsForRequest.add(jiraConsumer.get().getJiraRestUrl() + jqlQuery);
            argumentsForRequest.add(oauthVerifier);
            argumentsForRequest.add(accessToken);

            oAuthClient.execute(Command.fromString(commands.get(2)), argumentsForRequest);

            jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

            Issue issue = issueJsonParser.parse(jsonObject);

            bugs.add(issue);
        }

        bugs.stream()
                .collect(Collectors.groupingBy(bug -> bug.getCreationDate().getMonthOfYear(), Collectors.counting()))
                .forEach((id,count)->bugsPerMonth.put(id, count));
                        //System.out.println(id+"\t"+count));



        return null;
    }


}
