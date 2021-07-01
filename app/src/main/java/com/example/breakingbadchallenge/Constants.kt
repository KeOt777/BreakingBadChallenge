/**
 * Auxiliary File used to keep track and control of String Constants. This ensures little to no Hard-coded Strings
 * in the code itself.
 *
 * In case new String Constants are needed they should be added here
 *
 * String Constants are also useful in case they're needed to be reusable
 * */


package com.example.breakingbadchallenge

const val NO_NETWORK = "Network Not Available"

const val API_ERROR = "There was an error with retrieving information from the API"

const val API_OK = "OK"

const val API_BASE_URL = "https://www.breakingbadapi.com/api/"

const val API_CALL_ALL = "characters?"

const val API_CALL_FIRST = "characters?limit=1"

const val API_CALL_INITIAL_TEN = "characters?limit=10"

const val API_CALL_LIMIT = "characters?limit=@limit"

const val API_CALL_OFFSET = "characters?offset=@offset"

const val API_CALL_LIMIT_OFFSET = "characters?limit=@limit&offset=@offset"

const val CHARACTER_DATABASE = "characters-database"

const val CHARACTER_ID = "char_id"

const val CHARACTER_NAME = "name"

const val CHARACTER_NICKNAME = "nickname"

const val CHARACTER_OCCUPATION = "occupation"

const val CHARACTER_STATUS = "status"

const val CHARACTER_PORTRAYED = "portrayed"

const val CHARACTER_IMAGE = "img"

const val CHARACTER_FAVORITE = "isFavorite"

const val EMPTY_STRING = ""

const val DEFAULT_CHARACTER_ID = 0

const val JOIN_TO_STRING_SEPARATOR = "|"

const val SEPARATOR_REPLACEMENT = ", "

const val CLEAR_DATABASE = false