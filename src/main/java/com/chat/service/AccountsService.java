package com.chat.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsSimpleApi;
import org.openapitools.client.model.CustomerAccountsSimple;

public class AccountsService {
    private final AccountsSimpleApi simpleApi;

    public AccountsService(ApiClient client) {
        this.simpleApi = new AccountsSimpleApi(client);
    }

    public String firstSimpleAccountId(String customerId) throws ApiException {
        CustomerAccountsSimple res = simpleApi.getCustomerAccountsSimple(customerId);
        if (res.getAccounts().isEmpty()) {
            throw new IllegalStateException("No accounts found for customer " + customerId);
        }
        return res.getAccounts().get(0).getId();
    }
}
