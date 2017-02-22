package com.uom.jirareport.consumers.services;

import com.uom.jirareport.consumers.dto.ServiceResponse;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface JiraConsumerService {

    ServiceResponse getAuthorizationUrl(String url) throws Exception;
}
