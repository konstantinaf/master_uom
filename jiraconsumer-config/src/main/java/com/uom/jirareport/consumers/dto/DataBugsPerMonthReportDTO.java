package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class DataBugsPerMonthReportDTO implements Serializable {

    private String[] xData = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private BugsDataDTO[] yData;

    private DataBugsPerMonthReportDTO(DataDTOBuilder builder) {
        this.yData = builder.yData;
    }

    public static class DataDTOBuilder {
        private final BugsDataDTO[] yData;

        public DataDTOBuilder(BugsDataDTO[] yData) {
            this.yData = yData;
        }

        public DataBugsPerMonthReportDTO build() {
            return new DataBugsPerMonthReportDTO(this);
        }

    }

}
