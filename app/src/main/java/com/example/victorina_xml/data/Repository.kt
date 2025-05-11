package com.example.victorina_xml.data

import com.example.victorina_xml.presentation.Questions
import com.example.victorina_xml.data.QuestionsInstance.retrofit

class Repository {
    suspend fun getRandomQuestions(limit: Number,difficult: String): Questions {
        return retrofit.getRandomQuestions(limit,difficult)

    }

    suspend fun getQuestionReport(id: String) {
        return retrofit.getQuestionByID(id)
    }
    suspend fun getQuestionByCategory(limit: Number,category: String,difficulties: String): Questions{
        return retrofit.getQuestionsByCategory(limit = limit, categories =  category, difficulties = difficulties)
    }
}