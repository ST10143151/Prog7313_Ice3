package com.fake.quizgame

data class Question(
    val text: String,
    val options: List<String>,
    val answer: Int
)
