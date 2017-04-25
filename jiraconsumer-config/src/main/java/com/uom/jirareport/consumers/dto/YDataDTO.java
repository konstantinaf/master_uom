package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 03/03/2017.
 */
@Getter
public class YDataDTO implements Serializable {

    private String name;
    private double[] data;

    private YDataDTO(Builder builder) {
        this.name = builder.name;
        this.data = builder.data;
    }

    public static class Builder {
        private final String name;
        private final double[] data;

        public Builder(String name, double[] data) {
            this.name = name;
            this.data = data;
        }

        public YDataDTO build() {
            return new YDataDTO(this);
        }

    }
}
