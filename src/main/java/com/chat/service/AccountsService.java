package com.chat.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsSimpleApi;
import org.openapitools.client.model.CustomerAccountSimple;
import org.openapitools.client.model.CustomerAccountsSimple;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsService {
    private final AccountsSimpleApi simpleApi;

    public AccountsService(ApiClient client) {
        this.simpleApi = new AccountsSimpleApi(client);
    }

    public List<CustomerAccountSimple> firstSimpleAccountId(String customerId) throws ApiException {
        CustomerAccountsSimple res = simpleApi.getCustomerAccountsSimple(customerId);
        if (res.getAccounts().isEmpty()) {
            throw new IllegalStateException("No accounts found for customer " + customerId);
        }
        return res.getAccounts();
    }
}
