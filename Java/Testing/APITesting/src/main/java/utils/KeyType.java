package utils;

public enum KeyType {
    PUBLIC("public.reqres.api.key"),
    PRIVATE("private.reqres.api.key");

    private final String apiKey;

    KeyType(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
