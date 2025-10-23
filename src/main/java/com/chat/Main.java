package com.chat;
// Import classes:
import io.github.cdimascio.dotenv.Dotenv;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.AuthenticationApi;
import org.openapitools.client.api.CustomersApi;
import org.openapitools.client.auth.*;
import org.openapitools.client.model.*;
import org.openapitools.client.api.AccountValidationAssistanceApi;

public class Main {
    public static void main(String[] args) throws ApiException {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        // basic client + headers
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath("https://api.finicity.com");


        // App key is required on ALL calls
        String appKey = getenv("FINICITY_APP_KEY", dotenv);

        ApiKeyAuth finicityAppKey = (ApiKeyAuth) client.getAuthentication("FinicityAppKey");
        finicityAppKey.setApiKey(appKey);

        String partnerId = dotenv.get("FINICITY_PARTNER_ID");
        String partnerSecret = dotenv.get("FINICITY_PARTNER_SECRET");

        try{
            // Create a partner token
            AuthenticationApi authApi = new AuthenticationApi(client);
            var tokenResp = authApi.createToken(
                    new PartnerCredentials()
                            .partnerId(partnerId)
                            .partnerSecret(partnerSecret)
            );

            // Put token on client for subsequent calls
            ApiKeyAuth finicityAppToken = (ApiKeyAuth) client.getAuthentication("FinicityAppToken");
            finicityAppToken.setApiKey(tokenResp.getToken());

            // Create a testing customer (to obtain a customerID)
            CustomersApi customersApi = new CustomersApi(client);
            String uniqueUsername = "testuser_" + System.currentTimeMillis();

            // Build the proper request payload for creation
             NewCustomer newCustomer = new NewCustomer()
                .username(uniqueUsername)
                .email(uniqueUsername + "@example.com"); // optional but handy

            // In sandbox use addTestingCustomer; in prod use addCustomer
            Customer created = customersApi.addTestingCustomer(newCustomer);
            String customerId = created.getId();
            
            System.out.println("Created customerId: " + customerId);

            // 4) (Next) Generate a Connect URL for that customer
            //    Class names vary slightly by SDK version; typical pattern below:
            DataConnectApi dataConnectApi = new DataConnectApi(client);

            ConnectParameters params = new ConnectParameters()
                .customerId(customerId)
                .partnerId(partnerId)
                .redirectUri("https://yourapp.example.com/finicity/callback")
                .webhook("https://yourapp.example.com/finicity/webhook")
                // .experience("...") // choose the Connect experience if applicable
                // .fromDate(...), .singleUse(true), etc., depending on what you need
                ;

            ConnectUrlResponse link = dataConnectApi.generateConnectUrl(params);
            System.out.println("Connect URL: " + link.getUrl());

            // Send link.getUrl() to the user; after they finish, fetch statements by customerId.
            System.out.println(link.getUrl());
            //STILL TESTING UP TO CHANGES
            
        } catch (ApiException e) {
            System.err.println("API error: " + e.getCode());
            System.err.println("Error Message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    private static String getenv(String key, Dotenv dotenv){

        String v = System.getenv(key);
        if (v == null && dotenv != null) v = dotenv.get(key);
        return v;

    }
}
