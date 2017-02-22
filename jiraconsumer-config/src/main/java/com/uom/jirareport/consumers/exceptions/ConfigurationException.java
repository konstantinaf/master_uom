package com.uom.jirareport.consumers.exceptions;

/**
 * Created by fotarik on 13/02/2017.
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}
