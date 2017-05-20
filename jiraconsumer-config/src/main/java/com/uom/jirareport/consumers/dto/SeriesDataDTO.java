package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by fotarik on 20/05/2017.
 */
@Getter
public class SeriesDataDTO implements Serializable{

    private String name;
    private int y;
    private final boolean sliced = false;
    private final boolean selected = false;

    private SeriesDataDTO(Builder builder) {
        this.name = builder.name;
        this.y = builder.y;
    }

    public static class Builder {
        private final String name;
        private final int y;

        public Builder(String name, int y) {
            this.name = name;
            this.y = y;
        }

        public SeriesDataDTO build() {
            return new SeriesDataDTO(this);
        }

    }
}
