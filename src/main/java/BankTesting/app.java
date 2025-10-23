package BankTesting;

import io.github.cdimascio.dotenv.Dotenv;

public class app {
    public static void main(String[] args) {
        // Load env vars from .env
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String appKey = getenv("FINICITY_APP_KEY", dotenv);
        System.out.println("App started. FINICITY_APP_KEY present? " + (appKey != null));
    }

    private static String getenv(String key, Dotenv dotenv){

        String v = System.getenv(key);
        if (v == null && dotenv != null) v = dotenv.get(key);
        return v;

    }
}
