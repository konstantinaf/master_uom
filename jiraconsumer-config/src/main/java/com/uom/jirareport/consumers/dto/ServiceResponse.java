package com.uom.jirareport.consumers.dto;

import lombok.Getter;

/**
 * Created by fotarik on 21/02/2017.
 */
@Getter
public class ServiceResponse {
    private final String data; // required
    private final Integer error; // required
    private final String errorMessage; // required

    private ServiceResponse(ServiceResponseBuilder builder) {
        this.data = builder.data;
        this.error = builder.error;
        this.errorMessage = builder.errorMessage;

    }

    public static class ServiceResponseBuilder {
        private final String data;
        private final Integer error;
        private String errorMessage;

        public ServiceResponseBuilder(String data, Integer error, String errorMessage) {
            this.data = data;
            this.error = error;
            this.errorMessage = errorMessage;
        }

        public ServiceResponse build() {
            return new ServiceResponse(this);
        }

    }
}
