package com.bancocaminos.impactbackendapi.core.constants;

public class GenericApiConstants {
    public static final String IMPACT_API = "/impact";
    public static final String ID_SEQ = "id_seq";
    public static final String ALL_SUB_DOMAINS = "/**";
    public static final String SECOND_FACTOR = "/second-factor";
    public static final String SECOND_FACTOR_COMPLETE = IMPACT_API + SECOND_FACTOR;
    public static final String SECOND_FACTOR_COMPLETE_API = SECOND_FACTOR + ALL_SUB_DOMAINS;

    public class User {
        private User() {
        }

        public static final String USER_API = "/user";
        public static final String COMPLETE_USER_API = USER_API + ALL_SUB_DOMAINS;
    }

    public class ClientCampaignResult {
        private ClientCampaignResult() {
        }

        public static final String CLIENT_CAMPAIGN_RESULTS_API = "/client-campaign-result";
        public static final String CLIENT_CAMPAIGN_RESULTS_COMPLETE_API = CLIENT_CAMPAIGN_RESULTS_API
                + ALL_SUB_DOMAINS;
    }

}
