package com.chat.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.BankStatementsApi;
import org.openapitools.client.model.CustomerAccountSimple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StatementService {

    BankStatementsApi bankStatementsApi = new BankStatementsApi();

    public StatementService(ApiClient client){
        this.bankStatementsApi = new BankStatementsApi(client);
    }

    public List<File> pathToStatements(String customerId, String accountId)throws ApiException{

        List<File> files = new ArrayList<>();
        for(int i = 1; i <=4; i++){
            files.add(bankStatementsApi.getCustomerAccountStatement(customerId,accountId,i,"pdf"));
        }
        return files;
    }
}
