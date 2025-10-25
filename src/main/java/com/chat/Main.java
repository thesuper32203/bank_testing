package com.chat;
// Import classes:
import io.github.cdimascio.dotenv.Dotenv;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.*;
//import org.openapitools.client.api.DataConnectApi;
import org.openapitools.client.auth.*;
import org.openapitools.client.model.*;
import org.openapitools.client.model.CustomerAccounts;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;


public class Main {
    public static void main(String[] args) throws ApiException {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        // basic client + headers
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath("https://api.finicity.com");

        // App key is required on ALL calls
        String appKey = getenv("FINICITY_APP_KEY", dotenv);
        String partnerId = dotenv.get("FINICITY_PARTNER_ID");
        String partnerSecret = dotenv.get("FINICITY_PARTNER_SECRET");

        ApiKeyAuth finicityAppKey = (ApiKeyAuth) client.getAuthentication("FinicityAppKey");
        finicityAppKey.setApiKey(appKey);


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
            String uniqueUsername = "testuser_" + java.util.UUID.randomUUID();

            // Build the proper request payload for creation
             NewCustomer newCustomer = new NewCustomer()
                .username(uniqueUsername)
                .email(uniqueUsername + "@example.com"); // optional but handy

            // In sandbox use addTestingCustomer; in prod use addCustomer

            CreatedCustomer created = customersApi.addTestingCustomer(newCustomer);
            String customerId = created.getId();

            System.out.println("Created customerId: " + customerId);

            // 4) (Next) Generate a Connect URL for that customer
            //    Class names vary slightly by SDK version; typical pattern below:

            ReportCustomField loanField = new ReportCustomField()
                    .label("loanID")        // must match exactly
                    .value("LOAN-00123")
                    .shown(true);

            ReportCustomField branchField = new ReportCustomField()
                    .label("branch")
                    .value("New York")
                    .shown(false);


            ConnectApi dc = new ConnectApi(client);
            ConnectParameters p = new ConnectParameters()
                    .customerId(customerId)
                    .partnerId(partnerId)
                    .addReportCustomFieldsItem(loanField);

            var l = dc.generateConnectUrl(p);
            System.out.println("Link: " + l.getLink());


            /** LITE LINK
             * LiteConnectParameters params = new LiteConnectParameters()
             *                     .customerId(customerId)
             *                     .partnerId(partnerId)
             *                     .institutionId((long) 4222);
             */
            // var link = dc.generateLiteConnectUrl(params);
            // System.out.print(link.getLink());


            System.out.println("Press ENTER when you're done...");
            new Scanner(System.in).nextLine();

            //AccountsApi accountsApi = new AccountsApi(client);
            //CustomerAccounts accounts = accountsApi.getCustomerAccounts(customerId,null,null);
            //System.out.println(accounts.getAccounts());

            AccountsSimpleApi simpleAccount = new AccountsSimpleApi(client);
            CustomerAccountsSimple simAcc = simpleAccount.getCustomerAccountsSimple(customerId);
            String accountId = simAcc.getAccounts().get(0).getId();
            System.out.println("Account ID: " + accountId);

            BankStatementsApi bankStatementsApi = new BankStatementsApi(client);
            File statements = bankStatementsApi.getCustomerAccountStatement(customerId,accountId,4,"pdf");
            System.out.println(statements.exists());

            // Example: target folder for saving
            String folderPath = "C:/myapp/statements";   // or "./statements"
            String fileName = "statement_" + accountId + ".pdf";

            File dir = new File(folderPath);
            //Define target path
            Path targetPath = Path.of(folderPath,fileName);

            // Copy file to your directory
            try{
                Files.copy(statements.toPath(),targetPath);
                System.out.println("File created: " + targetPath.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }



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
