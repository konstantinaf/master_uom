package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class DataBugsPerMonthReportDTO implements Serializable {

    private String[] xData = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private BugsPerMonthDTO[] yData;
    private double giniCoefficient;

    private DataBugsPerMonthReportDTO(DataDTOBuilder builder) {
        this.yData = builder.yData;
        this.giniCoefficient = builder.giniCoefficient;

    }

    public static class DataDTOBuilder {
        private final BugsPerMonthDTO[] yData;
        private final double giniCoefficient;

        public DataDTOBuilder(BugsPerMonthDTO[] yData, double giniCoefficient) {
            this.yData = yData;
            this.giniCoefficient = giniCoefficient;
        }

        public DataBugsPerMonthReportDTO build() {
            return new DataBugsPerMonthReportDTO(this);
        }

    }

}
