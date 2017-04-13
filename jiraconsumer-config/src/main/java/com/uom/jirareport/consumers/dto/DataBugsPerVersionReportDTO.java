package com.uom.jirareport.consumers.dto;

/**
 * Created by fotarik on 13/04/2017.
 */
public class DataBugsPerVersionReportDTO {

    private String[] xData;
    private BugsDataDTO[] yData;

    private DataBugsPerVersionReportDTO(DataBugsPerVersionReportDTO.DataDTOBuilder builder) {
        this.xData = builder.xData;
        this.yData = builder.yData;
    }

    public static class DataDTOBuilder {
        private final String[] xData;
        private final BugsDataDTO[] yData;

        public DataDTOBuilder(String[] xData, BugsDataDTO[] yData) {
            this.xData = xData;
            this.yData = yData;
        }

        public DataBugsPerVersionReportDTO build() {
            return new DataBugsPerVersionReportDTO(this);
        }

    }
}
