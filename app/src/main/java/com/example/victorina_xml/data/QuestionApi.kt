package com.example.victorina_xml.data

import com.example.victorina_xml.presentation.Questions
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionApi {

    @GET("questions")
    suspend fun getRandomQuestions(
        @Query("limit") limit: Number,
      //  @Query("categories") categories: String = "",
        @Query("difficulties") difficulties: String
    ): Questions


    @GET("https://the-trivia-api.com/v2/question/{id}")
    suspend fun getQuestionByID(
        @Path("id") id: String,
        @Query("language") language: String = "fr"
    )

    @GET("questions")
    suspend fun getQuestionsByCategory(
        @Query("limit") limit: Number,
        @Query("categories") categories: String,
        @Query("difficulties") difficulties: String

    ): Questions
}