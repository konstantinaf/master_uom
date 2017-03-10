package com.uom.jirareport.consumers.dto;

/**
 * Created by fotarik on 10/03/2017.
 */
public class BugsPerMonthDataReportDTO extends DataReportDTO {

    private final YDataBugsPerMonthDTO[] yData;

        private BugsPerMonthDataReportDTO(Builder builder) {
        this.yData = builder.yData;
    }

    public static class Builder {
        private final YDataBugsPerMonthDTO[] yData;

        public Builder(YDataBugsPerMonthDTO[] yData) {
            this.yData = yData;
        }

        public BugsPerMonthDataReportDTO build() {
            return new BugsPerMonthDataReportDTO(this);
        }

    }
}
