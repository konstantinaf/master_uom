package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class DataBugsReportDTO implements Serializable {

    private String[] xData = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private YDataDTO[] yData;

    private DataBugsReportDTO(DataDTOBuilder builder) {
        this.yData = builder.yData;
    }

    public static class DataDTOBuilder {
        private final YDataDTO[] yData;

        public DataDTOBuilder(YDataDTO[] yData) {
            this.yData = yData;
        }

        public DataBugsReportDTO build() {
            return new DataBugsReportDTO(this);
        }

    }

}
