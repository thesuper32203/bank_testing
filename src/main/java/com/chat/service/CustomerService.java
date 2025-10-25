package com.chat.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CustomersApi;
import org.openapitools.client.model.CreatedCustomer;
import org.openapitools.client.model.NewCustomer;

import java.util.UUID;

public class CustomerService {

    private final CustomersApi customerApi;


    public CustomerService(ApiClient client) {
        this.customerApi = new CustomersApi(client);
    }

    // Sandbox helper: create a testing customer
    public String createTestingCustomer() throws ApiException{
        String username = "testuser_" + UUID.randomUUID();
        NewCustomer body = new NewCustomer()
                .username(username)
                .email(username+"@test.com");

        CreatedCustomer created = customerApi.addTestingCustomer(body);
        return created.getId();
    }

    /** Prod helper (if needed later): */
    // public String createCustomer(String username, String email) throws ApiException { ... }
}
