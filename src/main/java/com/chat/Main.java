package com.chat;
// Import classes:
import com.chat.config.EnvConfig;
import com.chat.sdk.FinicityClientFactory;
import com.chat.service.AuthService;
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

        EnvConfig cfg = EnvConfig.load(); //reads env + .env
        ApiClient client = FinicityClientFactory.create(cfg);

        AuthService auth = new AuthService(client,cfg);

        try{
            // create partner token
            String partnerToken = auth.createPartnerToken();
            System.out.println("partnerToken: " + partnerToken);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
