package com.uom.jirareport.consumers.oauth;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.ImmutableMap;
import com.uom.jirareport.consumers.dto.JiraConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import static com.uom.jirareport.consumers.oauth.PropertiesClient.*;

/**
 * Created by fotarik on 09/12/2016.
 */
@Slf4j
public class OAuthClient {

    private final Map<Command, Function<List<String>, Optional<Exception>>> actionHandlers;

    private  PropertiesClient propertiesClient;
    private JiraConsumer jiraConsumer;
    private final JiraOAuthClient jiraOAuthClient;
    @Getter
    private String authorizationUrl;
    @Getter
    private String accessToken;

    public OAuthClient(JiraOAuthClient jiraOAuthClient, JiraConsumer jiraConsumer) {
        try {
            propertiesClient  = new PropertiesClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jiraOAuthClient = jiraOAuthClient;
        this.jiraConsumer = jiraConsumer;

        actionHandlers = ImmutableMap.<Command, Function<List<String>, Optional<Exception>>>builder()
                .put(Command.REQUEST_TOKEN, this::handleGetRequestTokenAction)
                .put(Command.ACCESS_TOKEN, this::handleGetAccessToken)
                .put(Command.REQUEST, this::handleGetRequest)
                .build();
    }

    /**
     * Executes action (if found) with  given lists of arguments
     *
     * @param action
     * @param arguments
     */
    public void execute(Command action, List<String> arguments) {
        actionHandlers.getOrDefault(action, this::handleUnknownCommand)
                .apply(arguments)
                .ifPresent(Throwable::printStackTrace);
    }

    private Optional<Exception> handleUnknownCommand(List<String> arguments) {
        System.out.println("Command not supported. Only " + Command.names() + " are supported.");
        return Optional.empty();
    }

    /**
     * Gets request token and saves it to properties file
     *
     * @param arguments list of arguments: no arguments are needed here
     * @return
     */
    private Optional<Exception> handleGetRequestTokenAction(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();

        String requestToken;
        try {
            requestToken = jiraOAuthClient.getAndAuthorizeTemporaryToken(jiraConsumer.getConsumerKey(), jiraConsumer.getPrivateKey());
        } catch (NoSuchAlgorithmException e) {
            log.error("No Such Algorithm exception ", e);
            return Optional.of(e);
        } catch (InvalidKeySpecException e) {
            log.error("No Such Algorithm exception ", e);
            return Optional.of(e);
        } catch (IOException e) {
            log.error("No Such Algorithm exception ", e);
            return Optional.of(e);
        }
        authorizationUrl = jiraOAuthClient.getAuthorizationUrl() + "?oauth_token=" + requestToken;
        properties.put(REQUEST_TOKEN, requestToken);
        propertiesClient.savePropertiesToFile(properties);
        return Optional.empty();
    }

    /**
     * Gets access token and saves it to properties file
     *
     * @param arguments list of arguments: first argument should be secert (verification code provided by JIRA after request token authorization)
     * @return
     */
    private Optional<Exception> handleGetAccessToken(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        String tmpToken = arguments.get(0);
        String secret = arguments.get(1);

        try {
            accessToken = jiraOAuthClient.getAccessToken(tmpToken, secret, jiraConsumer.getConsumerKey(), jiraConsumer.getPrivateKey());
            properties.put(ACCESS_TOKEN, accessToken);
            properties.put(SECRET, secret);

            propertiesClient.savePropertiesToFile(properties);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Makes request to JIRA to provided url and prints response contect
     *
     * @param arguments list of arguments: first argument should be request url
     * @return
     */
    private Optional<Exception> handleGetRequest(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        String tmpToken = properties.get(ACCESS_TOKEN);
        String secret = properties.get(SECRET);
        String url = arguments.get(0);
        propertiesClient.savePropertiesToFile(properties);

        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            HttpResponse response = getResponseFromUrl(parameters, new GenericUrl(url));
            parseResponse(response);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Prints response content
     * if response content is valid JSON it prints it in 'pretty' format
     *
     * @param response
     * @throws IOException
     */
    private void parseResponse(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
        } catch (Exception e) {
            System.out.println(result);
        }
    }

    /**
     * Authanticates to JIRA with given OAuthParameters and makes request to url
     *
     * @param parameters
     * @param jiraUrl
     * @return
     * @throws IOException
     */
    private static HttpResponse getResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildGetRequest(jiraUrl);
        return request.execute();
    }
}