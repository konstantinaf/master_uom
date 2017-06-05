package com.uom.jira.consumers.utils;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.uom.jirareport.consumers.dto.*;
import javafx.util.Pair;
import org.apache.hadoop.util.hash.Hash;
import org.apache.spark.mllib.tree.impurity.Gini;
import scala.Int;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fotarik on 20/05/2017.
 */
public class ReportUtils {

    public static void initializeBugsPerMonthPerYearMap(Map<Integer, Double> bugsPerMonth) {

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

    public static DataBugsReportDTO buildDataResponseForChart(Map<Integer, Map<Integer, Double>> sortedMapCountBugsPerMonthPerYear) {
        int i=0;
        YDataDTO[] bugsPerMonthDTOs = new YDataDTO[sortedMapCountBugsPerMonthPerYear.size()];
        for (Map.Entry<Integer, Map<Integer, Double>> entry : sortedMapCountBugsPerMonthPerYear.entrySet()) {
            Integer year = entry.getKey();
            Map<Integer, Double> bugsPerMonthForYear = entry.getValue();

            List<Double> dataList = bugsPerMonthForYear.entrySet().stream()
                    .map(x -> x.getValue())
                    .collect(Collectors.toList());

            YDataDTO.Builder bugsPerMonthDTOBuilder = new YDataDTO.Builder(String.valueOf(year), convertListToDataArray(dataList));
            bugsPerMonthDTOs[i] = bugsPerMonthDTOBuilder.build();
            i++;
        }
        DataBugsReportDTO.DataDTOBuilder builder = new DataBugsReportDTO.DataDTOBuilder(bugsPerMonthDTOs);





        return builder.build();
    }

    public static Map<Integer, List<Issue>> countBugsPerYear(List<Issue> bugs) {
       return bugs.stream().collect(Collectors.groupingBy(bug -> bug.getCreationDate().getYear()));
    }

    public static Map<Integer, Map<Integer, Double>> countBugsPerMonthPerYear(Map<Integer, List<Issue>> bugsPerYear) {
        Map<Integer, Map<Integer, Double>> bugsPerMonthPerYear = new HashMap<>();

        for (Map.Entry<Integer, List<Issue>> entry : bugsPerYear.entrySet())
        {
            int year = entry.getKey();

            List<Issue> yearlyBugs = entry.getValue();

            Map<Integer, Double> bugsPerMonth = new HashMap<>();

            initializeBugsPerMonthPerYearMap(bugsPerMonth);
            //Create year map
            yearlyBugs.stream()
                    .collect(Collectors.groupingBy(bug -> bug.getCreationDate().getMonthOfYear(), Collectors.counting()))
                    .forEach((id, count) -> bugsPerMonth.put(id, Double.parseDouble(String.valueOf(count))));
           //count bugs per year per month
            bugsPerMonthPerYear.put(year, bugsPerMonth);

        }
        return bugsPerMonthPerYear;

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

    public static Map<String, Map<Integer, Double>> prepareDataForGiniRatio(Map<String, Map<Integer, Double>> bugsPerAssigneePerMonth) {
        Map<String, Map<Integer, Double>> bugsPerAssigneePerMonthWithGini = new HashMap<>();

        for (Map.Entry<String, Map<Integer, Double>> mapEntry : bugsPerAssigneePerMonth.entrySet()) {
            Map<Integer, Double> valuesPerAssignee = mapEntry.getValue();
            String assigneeName = mapEntry.getKey();

            double[] data = new double[12];
            int i = 0;
            for (Map.Entry<Integer, Double> entry : valuesPerAssignee.entrySet()) {
                data[i] = entry.getValue();
                i++;
            }
            double gini = calculateGiniCoefficient(data);
            bugsPerAssigneePerMonthWithGini.put(assigneeName + " g.f = " + gini, valuesPerAssignee);
        }
        return bugsPerAssigneePerMonthWithGini;
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

    private static double calculateGiniCoefficient(double[] data) {
        double giniRatio = Gini.calculate(data, data.length);
        System.out.println("Gini ratio is " + giniRatio);
        return giniRatio;
    }
}
