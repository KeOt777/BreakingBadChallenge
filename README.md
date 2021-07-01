# BreakingBadChallenge
WTA Android Senior Code Challenge created with Kotlin in Android Studio

# Basic Application Behaviour
	- The Application will always try to load the Local Datatable before attempting to consume the API
	- The API will only be consumed if there is no data in the Local Datatable
	
# Const Usage for API Calls
	- API_CALL_ALL: Retrieve all the Characters from the API
	- API_CALL_FIRST: Retrieve only the first Character from the API
	- API_CALL_INITIAL_TEN: Retrieve only the first 10 Characters from the API
	- API_CALL_LIMIT: Apply the "limit" parameter, replace @limit with the limit you wish
	- API_CALL_OFFSET: Apply the "offset" parameter, replace @offset with the offset you wish
	- API_CALL_LIMIT_OFFSET: Apply both the "limit" and the "offset" parameters, replace @limit and @offset 
		with the limit and offset you wish

# Clear Local Database

Set the CLEAR_DATABASE const to `true` to delete the local Database
