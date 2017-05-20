package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 20/05/2017.
 */
@Getter
public class PieReportDTO implements Serializable {

    private SeriesDataDTO[] data;

    private PieReportDTO(PieReportDTOBuilder builder) {
        this.data = builder.data;
    }

    public static class PieReportDTOBuilder {

        private final SeriesDataDTO[] data;


        public PieReportDTOBuilder(SeriesDataDTO[] data) {

            this.data = data;
        }

        public PieReportDTO build() {
            return new PieReportDTO(this);
        }

    }

}
