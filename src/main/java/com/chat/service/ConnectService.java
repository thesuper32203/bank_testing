package com.chat.service;

import com.chat.config.EnvConfig;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ConnectApi;
import org.openapitools.client.model.ConnectParameters;
import org.openapitools.client.model.ReportCustomField;

public class ConnectService {
    private final ConnectApi connectApi;
    private final String partnerId;

    public ConnectService(ApiClient client, EnvConfig cfg){
        this.connectApi = new ConnectApi(client);
        this.partnerId = cfg.getPartnerId();
    }

    public String generateConnectUrl(String customerId) throws ApiException {
        ReportCustomField loanField = new ReportCustomField()
                .label("Loan Amount")
                .value("Loan")
                .shown(true);

        ConnectParameters p = new ConnectParameters()
                .customerId(customerId)
                .partnerId(partnerId)
                .addReportCustomFieldsItem(loanField);

        var link = connectApi.generateConnectUrl(p);
        return link.getLink();
    }
}
