package com.bancocaminos.impactbackendapi.clientcampaign.results.rest.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class ClientCampaignResults {
    @NotNull
    private String client;

    @NotNull
    private String campaign;

    @NotNull
    private String results;

    @NotNull
    private Date resolvedDate;
}
