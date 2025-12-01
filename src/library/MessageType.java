package library;

public enum MessageType {
    // Auth
    LOGIN_ATTEMPT,
    LOGIN_RESPONSE,
    SIGNUP_ATTEMPT,
    SIGNUP_RESPONSE,
    LOGOUT_ATTEMPT,
    LOGOUT_RESPONSE,
    

    // Catalog
    CATALOG_SEARCH_REQ,
    CATALOG_SEARCH_RES,
    CATALOG_VIEW_REQ,
    CATALOG_VIEW_RES,
    ADD_RESOURCE_REQ,
    ADD_RESOURCE_RES,
    REMOVE_RESOURCE_REQ,
    REMOVE_RESOURCE_RES,

    // Member
    MEMBER_SEARCH_REQ,
    MEMBER_SEARCH_RES,
    MEMBER_INFO_REQ,
    MEMBER_INFO_RES,
    MEMBER_BORROWED_REQ,
    MEMBER_BORROWED_RES,

    // Checkout / Checkin
    CHECK_OUT_REQ,
    CHECK_OUT_RES,
    CHECK_IN_REQ,
    CHECK_IN_RES,

    // Generic
    ERROR,
    PING,
    
    //Window closes
    W_CLOSED;
    
}
