package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class DataReportDTO implements Serializable {

    private String[] xData = {"Jan","Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
   // private YDataBugsPerMonthDTO[] yData;
    //private double giniCoefficient;

//    private DataReportDTO(DataDTOBuilder builder) {
//      //  this.yData = builder.yData;
//       // this.giniCoefficient = builder.giniCoefficient;
//
//    }
//
//    public static class DataDTOBuilder {
//        private final YDataBugsPerMonthDTO[] yData;
//        private final double giniCoefficient;
//
//        public DataDTOBuilder(YDataBugsPerMonthDTO[] yData, double giniCoefficient) {
//            this.yData = yData;
//            this.giniCoefficient = giniCoefficient;
//        }
//
//        public DataReportDTO build() {
//            return new DataReportDTO(this);
//        }
//
//    }

}
