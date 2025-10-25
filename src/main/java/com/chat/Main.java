package com.chat;
// Import classes:
import com.chat.config.EnvConfig;
import com.chat.sdk.FinicityClientFactory;
import com.chat.service.*;
import com.chat.util.FileStorage;
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
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;


public class Main {
    public static void main(String[] args) throws ApiException {

        EnvConfig cfg = EnvConfig.load(); //reads env + .env
        ApiClient client = FinicityClientFactory.create(cfg);

        AuthService auth = new AuthService(client,cfg);
        CustomerService customerService = new CustomerService(client);
        ConnectService connectService = new ConnectService(client,cfg);
        AccountsService  accounts = new AccountsService(client);
        StatementService statements = new StatementService(client);
        FileStorage storage = new FileStorage(cfg.getStatementsDir());
        try{
            // 1) create partner token
            String partnerToken = auth.createPartnerToken();
            System.out.println("partnerToken: " + partnerToken);

            // 2) Create a sandbox customer (use .addCustomer for prod)
            String customerId = customerService.createTestingCustomer();
            System.out.println("customerId: " + customerId);

            // 3) generate connect URL (user complete auth in browser)
            String connectLink = connectService.generateConnectUrl(customerId);
            System.out.println("Connection link: " + connectLink);

            System.out.println("Press ENTER when the user finished linking...");
            new Scanner(System.in).nextLine();

            // 4) pull all accounts
            List<CustomerAccountSimple> accountList = accounts.firstSimpleAccountId(customerId);
            // loop through each account

            for(CustomerAccountSimple account : accountList){
                String accountId = account.getId();
                System.out.println("Account Id: " + accountId);
                //retrieve all statements for current accuont
                List<File> statementFilesList = statements.pathToStatements(customerId,accountId);
                int k = 1;
                for(File file : statementFilesList){
                    String filename = "statement_" + accountId + "_" + k + ".pdf";
                    storage.save(file, filename);
                    k++;
                }
                // save each statement file with account id in the file name



            }



        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
