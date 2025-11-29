package library;

public enum MessageType { //REQ = REQUEST, RES = RESPONSE
	//Login  , Log out,
	LOGIN_ATTEMPT,  //sent from GUI, content is LoginInfo
	LOGIN_SUCCESS,
	LOGIN_FAIL,
	LOGOUT_ATTEMPT, //sent from GUI, content ____
	LOGOUT_RESPONSE,
	
	//Signing up 
	SIGNUP_ATTEMPT, //sent from GUI
	SIGNUP_SUCCESS,
	SIGNUP_FAIL,
	
	
	//Catalog search/view
	CATALOG_SEARCH_REQ,  //sent from GUI, content is string title
	CATALOG_S_RES,
	CATALOG_VIEW_REQ, //sent from GUI, content is ____
	CATALOG_VIEW_RES,
	
	//Item operations
	CHECK_IN_REQ, //sent from GUI, 
	CHECK_IN_RES,
	
	CHECK_OUT_REQ, //sent from GUI
	CHECK_OUT_RES,
	
	
	
	MEMBER_DATA_REQUEST,
	
	SEARCH_MEMBER,
	SEARCH_RESULTS,
	
	
	
	
	
	
	ERROR
	
}
