package com.fake.quizgame

data class HistoryEntry(
    val username: String,
    val category: String,
    val score: Int,
    val correctAnswers: Int,
    val timestamp: Long
)
