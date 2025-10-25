package com.chat.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private final String basePath;
    private final String appKey;
    private final String partnerId;
    private final String partnerSecret;
    private final String statementsDir;


    public EnvConfig(String basePath, String appKey, String partnerId, String partnerSecret, String statementsDir) {
        this.basePath = basePath;
        this.appKey = appKey;
        this.partnerId = partnerId;
        this.partnerSecret = partnerSecret;
        this.statementsDir = statementsDir;
    }

    public static EnvConfig load(){
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String basePath = "https://api.finicity.com";
        String appKey = getenv("FINICITY_APP_KEY",dotenv);
        String partnerId = getenv("FINICITY_PARTNER_ID",dotenv);
        String partnerSecret = getenv("FINICITY_PARTNER_SECRET",dotenv);
        String statementsDir = "C:\\myapp\\statements";

        return new EnvConfig(basePath, appKey, partnerId, partnerSecret, statementsDir);
    }

    public static String getenv(String key, Dotenv dotenv){

        String v  = System.getenv(key);
        if (v == null && dotenv != null) v = dotenv.get(key);
        return v;

    }


    public String getBasePath()      { return basePath; }
    public String getAppKey()        { return appKey; }
    public String getPartnerId()     { return partnerId; }
    public String getPartnerSecret() { return partnerSecret; }
    public String getStatementsDir() { return statementsDir; }
}

