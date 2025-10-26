package com.chat.sdk;

import com.chat.config.EnvConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.ApiKeyAuth;

public class FinicityClientFactory {

    public static ApiClient create(EnvConfig cfg){
        ApiClient client = Configuration.getDefaultApiClient();
        //AppKey header for all calls
        ApiKeyAuth appKey = (ApiKeyAuth) client.getAuthentication("FinicityAppKey");
        appKey.setApiKey(cfg.getAppKey());
        // Token header will be set after we create a partner token
        return client;
    }
    // helper for setting the partner token header after authentication
    public static void setPartnerToken(ApiClient client, String token){
        ApiKeyAuth appToken = (ApiKeyAuth) client.getAuthentication("FinicityAppToken");
        appToken.setApiKey(token);
    }
}
