package com.fake.quizgame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    private val questions = mutableListOf<Question>()
    val currentQuestion = MutableLiveData<Question>()
    private var index = 0
    var correctCount = 0

    fun loadQuestions(category: String) {
        questions.clear()
        for (i in 1..10) {
            questions.add(
                Question(
                    text = "$category Question $i",
                    options = listOf("A", "B", "C", "D"),
                    answer = 0
                )
            )
        }
        index = 0
        correctCount = 0
        currentQuestion.value = questions[index]
    }

    fun answer(selected: Int) {
        if (questions[index].answer == selected) correctCount++
    }

    fun nextQuestion() {
        index++
        currentQuestion.value = questions.getOrNull(index)
    }

    fun hasNext() = index + 1 < questions.size
    fun progress() = ((index + 1) * 10)
    fun calculateScore(time: Long): Int = correctCount * 1000 - (time / 100).toInt()
}
