package com.example.victorina_xml.presentation


class Questions : ArrayList<QuestionsItem>()

data class QuestionsItem(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<Any>,
    val isNiche: Boolean,
    val question: Question,
    val regions: List<Any>,
    val tags: List<Any>,
    val type: String
)
//object Question  {
//    data class Q(val question: String)
//
//}

data class Question (val text: String)
