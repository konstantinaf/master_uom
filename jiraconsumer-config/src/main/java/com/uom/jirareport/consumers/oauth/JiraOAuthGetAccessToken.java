package com.uom.jirareport.consumers.oauth;

import com.google.api.client.auth.oauth.OAuthGetAccessToken;

/**
 * Created by fotarik on 09/12/2016.
 */
public class JiraOAuthGetAccessToken extends OAuthGetAccessToken {

    /**
     * @param authorizationServerUrl encoded authorization server URL
     */
    public JiraOAuthGetAccessToken(String authorizationServerUrl) {
        super(authorizationServerUrl);
        this.usePost = true;
    }
}
