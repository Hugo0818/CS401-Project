package library;

public enum MessageType {
    // Auth
    LOGIN_ATTEMPT,
    LOGIN_RESPONSE,
    

    SIGNUP_ATTEMPT,
    SIGNUP_RESPONSE,
    
    SIGNUP_SUCCESS,
    SIGNUP_FAIL,

    LOGOUT_ATTEMPT,
    LOGOUT_RESPONSE,

    // Catalog
    CATALOG_SEARCH_REQ,
    CATALOG_SEARCH_RES,
    CATALOG_VIEW_REQ,
    CATALOG_VIEW_RES,

    // Member
    MEMBER_SEARCH_REQ,
    MEMBER_SEARCH_RES,
    MEMBER_INFO_REQ,
    MEMBER_INFO_RES,

    // Checkout / Checkin
    CHECK_OUT_REQ,
    CHECK_OUT_RES,
    CHECK_IN_REQ,
    CHECK_IN_RES,

    // Generic
    ERROR,
    PING
}
