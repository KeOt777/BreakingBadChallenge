package com.example.breakingbadchallenge.apihandler

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * API Service definition
 * */

interface APIService {
    @GET
    suspend fun getCharacters(@Url url:String): Response<List<CharacterResponse>>
}