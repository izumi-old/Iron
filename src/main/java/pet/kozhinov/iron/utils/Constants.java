package pet.kozhinov.iron.utils;

public final class Constants {
    public static final String API_PREFIX_V1 = "/iron/v1";

    public static final String API_PREFIX = API_PREFIX_V1;
    public static final String API_LOGIN_URL = API_PREFIX + "/login";
    public static final String API_SIGNUP_URL = API_PREFIX + "/person";
    public static final String API_LOGOUT_URL = API_PREFIX + "/logout";
    public static final String CURRENT_PERSON_KEYWORD = "current";
    public static final String FRONTEND_ORIGIN = "http://localhost:3000";

    public static final long SECURITY_EXPIRATION_TIME_HOURS = 240;
    public static final long SECURITY_EXPIRATION_TIME_MILLISECONDS = SECURITY_EXPIRATION_TIME_HOURS * 3600 * 1000;
    public static final String SECURITY_SECRET = "myironsecretIloveapples";
    public static final String SECURITY_HEADER_STRING = "Authorization";
    public static final String SECURITY_TOKEN_PREFIX = "Bearer ";

    public static final int MONTHS_IN_YEAR = 12;
    public static final int ANNUITY_COEFFICIENT = 1;
    public static final int MAX_PERCENTS = 100;

    public static final String HANDLER_TIMESTAMP_PARAMETER = "timestamp";
    public static final String HANDLER_STATUS_PARAMETER = "status";
    public static final String HANDLER_ERRORS_PARAMETER = "errors";
    public static final String HANDLER_MESSAGE_PARAMETER = "message";

    private Constants() {}
}
