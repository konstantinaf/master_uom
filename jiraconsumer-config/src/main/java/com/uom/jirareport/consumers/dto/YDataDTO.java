package com.uom.jirareport.consumers.dto;

import lombok.Getter;

/**
 * Created by fotarik on 10/03/2017.
 */
@Getter
public class YDataDTO {

    private final String name;
    private final double[] data;

    public static class Builder<T extends Builder> {

        private String name = "";
        private double[] data =null;

        public Builder() {}

        public T builder(String name, double[] data) {
            this.name = name;
            this.data = data;
            return (T) this;
        }

        public YDataDTO build() { return new YDataDTO(this); }
    }

    protected YDataDTO(Builder builder) {
        name = builder.name;
        data = builder.data;
    }
}