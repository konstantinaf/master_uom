package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class DataDTO implements Serializable {

    private String[] xData = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private BugsPerMonthDTO yData;

    private DataDTO(DataDTOBuilder builder) {
        this.yData = builder.yData;

    }

    public static class DataDTOBuilder {
        private final BugsPerMonthDTO yData;

        public DataDTOBuilder(BugsPerMonthDTO yData) {
            this.yData = yData;
        }

        public DataDTO build() {
            return new DataDTO(this);
        }

    }

}
