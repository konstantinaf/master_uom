package com.uom.jira.consumers.utils;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.uom.jirareport.consumers.dto.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fotarik on 20/05/2017.
 */
public class ReportUtils {

    public static void initializeBugsPerMonthMap(Map<Integer, Double> bugsPerMonth) {
        //for each month initially put 0 bugs
        for (int i = 1; i < 13; i++) {
            bugsPerMonth.put(i, new Double(0));
        }
    }

    public static void sortBugsPerMonthMapByMonthNumber(Map<Integer, Double> bugsPerMonth, List<Double> dataList) {
        Map<Integer, Double> map = new TreeMap<>(bugsPerMonth);


        for (Integer key : map.keySet()) {
            dataList.add(map.get(key));
        }
    }

    public static double[] convertListToDataArray(List<Double> dataList) {

        double[] dataArray = new double[dataList.size()];
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = dataList.get(i).doubleValue();
        }
        return dataArray;
    }

    public static DataBugsReportDTO buildDataResponseForChart(String projectKey, List<Double> dataList) {
        YDataDTO.Builder bugsPerMonthDTOBuilder = new YDataDTO.Builder(projectKey, convertListToDataArray(dataList));

        YDataDTO[] bugsPerMonthDTOs = new YDataDTO[1];
        bugsPerMonthDTOs[0] = bugsPerMonthDTOBuilder.build();
        DataBugsReportDTO.DataDTOBuilder builder = new DataBugsReportDTO.DataDTOBuilder(bugsPerMonthDTOs);

        return builder.build();
    }

    public static void countBugsPerMonth(List<Issue> bugs, Map<Integer, Double> bugsPerMonth) {
        bugs.stream()
                .collect(Collectors.groupingBy(bug -> bug.getCreationDate().getMonthOfYear(), Collectors.counting()))
                .forEach((id, count) -> bugsPerMonth.put(id, Double.parseDouble(String.valueOf(count))));
    }

    public static Map<String, Map<Integer, Double>> countBugsPerAssigneePerMonth(List<Issue> bugs) {
        Map<String, Map<Integer, Double>> bugsPerAssigneePerMonth = new HashMap<>();

        Map<String, List<Issue>> mapByAssignee =
                bugs.stream().collect(Collectors.groupingBy(bug -> bug.getAssignee().getName()));

        Iterator it = mapByAssignee.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            System.out.println(pair.getKey() + " = " + pair.getValue());

            List<Issue> assigneeBugs = (List<Issue>) pair.getValue();

            Map<Integer, Double> bugsPerMonth = new HashMap<>();

            initializeBugsPerMonthMap(bugsPerMonth);

            countBugsPerMonth(assigneeBugs, bugsPerMonth);

            bugsPerAssigneePerMonth.put((String) pair.getKey(), bugsPerMonth);

            it.remove(); // avoids a ConcurrentModificationException
        }

        return bugsPerAssigneePerMonth;

    }

    public static List<BugVersion> createListOfBugVersion(List<Issue> bugs) {
        BugVersion bugVersion;
        List<BugVersion> bugVersions = new ArrayList<>();
        for (Issue issue : bugs) {
            for (Version affectedVersion : issue.getAffectedVersions()) {
                bugVersion = new BugVersion();
                bugVersion.setKey(issue.getKey());
                bugVersion.setAffectedVersion(affectedVersion.getName());
                bugVersions.add(bugVersion);
            }
        }
        return bugVersions;
    }

    public static Map<String, Double> countBugsPerAffectedVersion(List<BugVersion> bugVersions) {
        Map<String, Double> bugsPerVersion = new HashMap<>();

        bugVersions.stream()
                .collect(Collectors.groupingBy(bug -> bug.getAffectedVersion(), Collectors.counting()))
                .forEach((id, count) -> bugsPerVersion.put(id, Double.parseDouble(String.valueOf(count))));
        return  bugsPerVersion;

    }

    public static DataBugsReportDTO buildResponseForAssigneeBugs(Map<String, Map<Integer, Double>> bugsPerAssigneePerMonth) {

        YDataDTO[] yDataDTOS = new YDataDTO[bugsPerAssigneePerMonth.size()];
        Iterator it = bugsPerAssigneePerMonth.entrySet().iterator();
        List<Double> dataList;
        int i = 0;
        while (it.hasNext()) {
            dataList = new ArrayList<>();
            Map.Entry pair = (Map.Entry) it.next();

            sortBugsPerMonthMapByMonthNumber((Map<Integer, Double>) pair.getValue(), dataList);

            YDataDTO.Builder yDataDTOBuilder = new YDataDTO.Builder((String) pair.getKey(), convertListToDataArray(dataList));
            yDataDTOS[i] = yDataDTOBuilder.build();
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }

        DataBugsReportDTO.DataDTOBuilder dataBugsReportDTO = new DataBugsReportDTO.DataDTOBuilder(yDataDTOS);
        return dataBugsReportDTO.build();

    }

    public static PieReportDTO buildDataResponseForPieChart(Map<String, Double> bugsPerVersion) {

        Iterator it = bugsPerVersion.entrySet().iterator();
        SeriesDataDTO[] dataDTO = new SeriesDataDTO[bugsPerVersion.size()];
        int i=0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String versionName = (String) pair.getKey();
            Double numberOfBugs = (Double) pair.getValue();
            int[] numOfBgus = new int[1];
            numOfBgus[0] = numberOfBugs.intValue();

            SeriesDataDTO.Builder dataDTOBuilder = new SeriesDataDTO.Builder(versionName, numOfBgus);
            dataDTO[i] = dataDTOBuilder.build();
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }

        PieReportDTO.PieReportDTOBuilder builder = new PieReportDTO.PieReportDTOBuilder(dataDTO);

        return builder.build();

    }


    public static void excludeBugsWithoutAssignee(List<Issue> bugs) {
        Iterator<Issue> iter = bugs.iterator();

        while (iter.hasNext()) {
            Issue issue = iter.next();

            if (issue.getAssignee() == null)
                iter.remove();
        }
    }


    public static void excludeBugsWithoutAffectedVersion(List<Issue> bugs) {
        Iterator<Issue> iter = bugs.iterator();

        while (iter.hasNext()) {
            Issue issue = iter.next();

            if (issue.getAffectedVersions() == null)
                iter.remove();
        }
    }
}
