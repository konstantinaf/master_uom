package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 20/05/2017.
 */
@Getter
public class SeriesDataDTO implements Serializable{

    private String text;
    private int[] values;


    private SeriesDataDTO(Builder builder) {
        this.text = builder.text;
        this.values = builder.values;
    }

    public static class Builder {
        private final String text;
        private final int[] values;

        public Builder(String text, int[] values) {
            this.text = text;
            this.values = values;
        }

        public SeriesDataDTO build() {
            return new SeriesDataDTO(this);
        }

    }
}
