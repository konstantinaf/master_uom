package com.uom.jirareport.consumers.oauth;

import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;

/**
 * Created by fotarik on 09/12/2016.
 */
public class JiraOAuthGetTemporaryToken extends OAuthGetTemporaryToken {

    /**
     * @param authorizationServerUrl encoded authorization server URL
     */
    public JiraOAuthGetTemporaryToken(String authorizationServerUrl) {
        super(authorizationServerUrl);
        this.usePost = true;
    }
}
