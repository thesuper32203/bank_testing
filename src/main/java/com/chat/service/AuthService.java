package com.chat.service;

import com.chat.config.EnvConfig;
import com.chat.sdk.FinicityClientFactory;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AuthenticationApi;
import org.openapitools.client.model.PartnerCredentials;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final ApiClient client;
    private final EnvConfig cfg;

    public AuthService(ApiClient client, EnvConfig cfg) {
        this.client = client;
        this.cfg = cfg;
    }

    //creates partner token and sets it on the ApiClient for subsequent calls
    public String createPartnerToken() throws ApiException {
        AuthenticationApi auth = new  AuthenticationApi(client);
        var resp = auth.createToken(
                new PartnerCredentials()
                        .partnerId(cfg.getPartnerId())
                        .partnerSecret(cfg.getPartnerSecret())
        );
        String token = resp.getToken();
        FinicityClientFactory.setPartnerToken(client,token);
        return token;
    }
}
