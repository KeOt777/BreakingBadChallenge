package com.example.breakingbadchallenge

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getCharacters(@Url url:String): Response<List<CharacterResponse>>
}