package com.bancocaminos.impactbackendapi.clientcampaign.results.rest.model;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ClientCampaignResultsRequestModel {
    @NotNull
    private List<ClientCampaignResults> clientCampaignResults;
}
