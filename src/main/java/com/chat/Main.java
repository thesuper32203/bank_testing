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
            Customer customer = new Customer().id(uniqueUsername).type("testing");
            System.out.print(customer.getId());

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