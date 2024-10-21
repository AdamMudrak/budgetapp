package com.example.budgetingapp.constants.config;

public class ConfigConstants {
    public static final String COMPONENT_MODEL = "spring";

    public static final String SECURITY_SCHEME_KEY = "BearerAuth";
    public static final String SECURITY_SCHEME = "bearer";
    public static final String BEARER_FORMAT = "JWT";

    public static final String FRONT_END_LOCAL_ALLOWED = "${frontend.local-allowed}";
    public static final String FRONT_END_REMOTE_ALLOWED = "${frontend.remote-allowed}";
    public static final String ALLOWED_METHODS = "*";
    public static final String ALLOWED_HEADERS = "*";

    public static final String AUTH_MATCHER = "/auth/**";
    public static final String SWAGGER_MATCHER = "/swagger-ui/**";
    public static final String SWAGGER_DOCS_MATCHER = "/v3/api-docs/**";
    public static final String ERRORS_MATCHER = "/errors";
}
