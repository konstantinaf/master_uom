package com.uom.jirareport.consumers.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import com.uom.jirareport.consumers.dao.JiraConsumerRepository;
import com.uom.jirareport.consumers.dto.*;
import com.uom.jirareport.consumers.oauth.Command;
import com.uom.jirareport.consumers.oauth.JiraOAuthClient;
import com.uom.jirareport.consumers.oauth.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.mllib.tree.impurity.Gini;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fotarik on 13/02/2017.
 */
@Slf4j
@Service
public class JiraConsumerServiceImpl implements JiraConsumerService {

    private String accessToken;
    private Optional<JiraConsumer> jiraConsumer;
    private OAuthClient oAuthClient;
    private List<String> argumentsForRequest;
    private final static String REQUEST_TOKEN = "requestToken";
    private final static String ACCESS_TOKEN = "accessToken";
    private final static String REQUEST = "request";
    private final static String JQL_PROJECT = "project";
    private final static String JQL_ISSUES_BY_PROJECT = "search?jql=project" + "%20%3D%20";
    private final static String FIELDS_TO_SHOW = "&fields=id,key";
    private final static String JQL_TYPE_BUG = "%20AND%20issuetype%20%3D%20bug";
    private final static String JQL_ISSUE = "issue/";
    private final static String JQL_EXPAND = "?expand=names,schema";
    private final static String JQL_CREATED = "%20AND%20created%20%3E%20";
    private static String lastDayOfLastYear;

    @Autowired
    JiraConsumerRepository jiraConsumerRepository;

    static {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);
        prevYear.set(Calendar.MONTH, 11);
        prevYear.set(Calendar.DAY_OF_MONTH, 31);
        Date date = prevYear.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        lastDayOfLastYear = format1.format(date);
        System.out.println(lastDayOfLastYear);

    }

    @Override
    public ServiceResponse getAuthorizationUrl(String domainName) throws Exception {
        jiraConsumer = Optional.ofNullable(jiraConsumerRepository.findJiraConsumerByHomeUrl(domainName));

        prepareArguments(null, ACCESS_TOKEN, REQUEST);

        ServiceResponse.ServiceResponseBuilder builder;
        try {
            if (jiraConsumer.isPresent()) {
                oAuthClient = new OAuthClient(new JiraOAuthClient(jiraConsumer.get().getJiraConsumerKey().getJiraUrl()), jiraConsumer.get());
                executeJiraHttpRequest(REQUEST_TOKEN, argumentsForRequest);
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

        prepareArguments(oauthToken, oauthVerifier, null);

        try {
            if (jiraConsumer.isPresent()) {
                executeJiraHttpRequest(ACCESS_TOKEN, argumentsForRequest);
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

        String projectJQLQuery = JQL_PROJECT;
        prepareArguments(jiraConsumer.get().getJiraRestUrl()+ projectJQLQuery, oauthVerifier, accessToken);
        executeJiraHttpRequest(REQUEST, argumentsForRequest);

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
        String jqlQuery = JQL_ISSUES_BY_PROJECT + projectKey + FIELDS_TO_SHOW;

        prepareArguments(jiraConsumer.get().getJiraRestUrl() + jqlQuery, oauthVerifier, accessToken);
        executeJiraHttpRequest(REQUEST, argumentsForRequest);

        JSONObject jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

        ObjectMapper mapper = new ObjectMapper();
        IssueResponseDTO issueResponseDTO = mapper.readValue(jsonObject.toString(), IssueResponseDTO.class);

        IssueJsonParser issueJsonParser = new IssueJsonParser();

        for (IssueDTO issueDTO : issueResponseDTO.getIssues()) {
            jqlQuery = JQL_ISSUE +issueDTO.getKey() + JQL_EXPAND;
            argumentsForRequest = new ArrayList<>();
            prepareArguments(jiraConsumer.get().getJiraRestUrl() + jqlQuery, oauthVerifier, accessToken);
            executeJiraHttpRequest(REQUEST, argumentsForRequest);

            jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());
            Issue issue = issueJsonParser.parse(jsonObject);
            issues.add(issue);
        }

        return  issues;
    }
    //TODO GEt bugs Per Release
    @Override
    public DataBugsPerMonthReportDTO getBugsCountPerMonth(String projectKey, String oauthVerifier) throws Exception {
        Map<Integer, Double> bugsPerMonth = new HashMap<>();
        List<Issue> bugs = new ArrayList<>();
        List<Double> dataList = new ArrayList<>();

        String jqlQuery = JQL_ISSUES_BY_PROJECT + projectKey + JQL_TYPE_BUG + JQL_CREATED + lastDayOfLastYear + FIELDS_TO_SHOW;

        prepareArguments(jiraConsumer.get().getJiraRestUrl()+jqlQuery, oauthVerifier, accessToken);
        executeJiraHttpRequest(REQUEST, argumentsForRequest);

        JSONObject jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

        ObjectMapper mapper = new ObjectMapper();
        IssueResponseDTO issueResponseDTO = mapper.readValue(jsonObject.toString(), IssueResponseDTO.class);

        IssueJsonParser issueJsonParser = new IssueJsonParser();

        for (IssueDTO issueDTO : issueResponseDTO.getIssues()) {
            jqlQuery = JQL_ISSUE +issueDTO.getKey()+ JQL_EXPAND;
            prepareArguments(jiraConsumer.get().getJiraRestUrl()+jqlQuery, oauthVerifier, accessToken);
            executeJiraHttpRequest(REQUEST, argumentsForRequest);

            jsonObject = new JSONObject(oAuthClient.getHttpResponse().parseAsString());

            Issue issue = issueJsonParser.parse(jsonObject);

            bugs.add(issue);
        }
        initializeBugsPerMonthMap(bugsPerMonth);

        countBugsAndUpdateBugsPerMonthMap(bugs, bugsPerMonth);

        sortBugsPerMonthMapByMonthNumber(bugsPerMonth, dataList);

        return buildDataResponseForChart(projectKey, dataList);
    }

    private void executeJiraHttpRequest(String command, List<String> argumentsForRequest) {
        oAuthClient.execute(Command.fromString(command), argumentsForRequest);
    }


    private void prepareArguments(String firstArgument, String secondArgument, String thirdArgument) {
        argumentsForRequest = new ArrayList<>();
        if (firstArgument != null) {
            argumentsForRequest.add(firstArgument);
        }
        if (secondArgument != null) {
            argumentsForRequest.add(secondArgument);
        }
        if (thirdArgument != null) {
            argumentsForRequest.add(thirdArgument);
        }
    }

    private void countBugsAndUpdateBugsPerMonthMap(List<Issue> bugs, Map<Integer, Double> bugsPerMonth) {
        bugs.stream()
                .collect(Collectors.groupingBy(bug -> bug.getCreationDate().getMonthOfYear(), Collectors.counting()))
                .forEach((id, count)->bugsPerMonth.put(id, Double.parseDouble(String.valueOf(count))));
    }

    private void initializeBugsPerMonthMap(Map<Integer, Double> bugsPerMonth) {
        //for each month initially put 0 bugs
        for (int i=1; i<13; i++) {
            bugsPerMonth.put(i, new Double(0));
        }
    }

    private void sortBugsPerMonthMapByMonthNumber(Map<Integer, Double> bugsPerMonth, List<Double> dataList) {
        Map<Integer, Double> map = new TreeMap<>(bugsPerMonth);

        for (Integer key: map.keySet()) {
            dataList.add(map.get(key));
        }
    }

    private double[] convertListToDataArray(List<Double> dataList) {

        double[] dataArray = new double[dataList.size()];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = dataList.get(i).doubleValue();  // java 1.4 style
            // or:
            dataArray[i] = dataList.get(i);                // java 1.5+ style (outboxing)
        }
        return dataArray;
    }

    private DataBugsPerMonthReportDTO buildDataResponseForChart(String projectKey, List<Double> dataList) {
        BugsPerMonthDTO.BugsPerMonthDTOBuilder bugsPerMonthDTOBuilder = new BugsPerMonthDTO.BugsPerMonthDTOBuilder(projectKey, convertListToDataArray(dataList));

        BugsPerMonthDTO[] bugsPerMonthDTOs = new BugsPerMonthDTO[1];
        bugsPerMonthDTOs[0] = bugsPerMonthDTOBuilder.build();
        DataBugsPerMonthReportDTO.DataDTOBuilder builder = new DataBugsPerMonthReportDTO.DataDTOBuilder(bugsPerMonthDTOs, calculateGiniCoefficient(bugsPerMonthDTOs[0].getData()));

        return builder.build();
    }

    private double calculateGiniCoefficient(double[] yDataData) {
        double giniRatio = Gini.calculate(yDataData, yDataData.length);
        System.out.println("Gini ratio is " + giniRatio);
        return giniRatio;
    }

}
