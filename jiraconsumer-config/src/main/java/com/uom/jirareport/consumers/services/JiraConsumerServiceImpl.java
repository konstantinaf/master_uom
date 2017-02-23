package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Project;
import com.uom.jirareport.consumers.dao.JiraConsumerRepository;
import com.uom.jirareport.consumers.dto.JiraConsumer;
import com.uom.jirareport.consumers.dto.ServiceResponse;
import com.uom.jirareport.consumers.oauth.Command;
import com.uom.jirareport.consumers.oauth.JiraOAuthClient;
import com.uom.jirareport.consumers.oauth.OAuthClient;
import com.uom.jirareport.consumers.oauth.PropertiesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by fotarik on 13/02/2017.
 */
@Slf4j
@Service
public class JiraConsumerServiceImpl implements JiraConsumerService {

    private static String accessToken = "";
    final static List<String> commands = new ArrayList<>();
    static Optional<JiraConsumer> jiraConsumer;

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
        OAuthClient oAuthClient;
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
        List<String> argumentsWithoutSecond = new ArrayList<>();
        argumentsWithoutSecond.add(oauthToken);
        argumentsWithoutSecond.add(oauthVerifier);

        ServiceResponse.ServiceResponseBuilder builder;
        OAuthClient oAuthClient;
        try {
            if (jiraConsumer.isPresent()) {
                oAuthClient = new OAuthClient(new JiraOAuthClient(jiraConsumer.get().getJiraConsumerKey().getJiraUrl()), jiraConsumer.get());
                oAuthClient.execute(Command.fromString(commands.get(1)), argumentsWithoutSecond);
                accessToken = oAuthClient.getAccessToken();
            }
        } catch (Exception e) {
            log.error("Cannot create OAuthClient ", e);
            throw e;
        }

    }

    @Override
    public List<Project> getDomainProjectsFromJira(String oauthToken, String oauthVerifier) throws Exception {
        this.getAccessToken(oauthToken, oauthVerifier);
        System.out.print(accessToken);
        return null;
    }


}
