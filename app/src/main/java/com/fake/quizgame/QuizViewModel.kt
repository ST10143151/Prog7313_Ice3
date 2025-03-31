package com.fake.quizgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    fun loadQuestions(category: String) {
        val questionList = when (category) {
            "General Knowledge" -> listOf(
                Question("Capital of France?", listOf("Paris", "Berlin", "London", "Rome"), 0),
                Question("Largest ocean?", listOf("Pacific", "Atlantic", "Indian", "Arctic"), 0),
                Question("Color of the sun?", listOf("Yellow", "Blue", "Red", "White"), 0)
            )
            "Science" -> listOf(
                Question("H2O is?", listOf("Water", "Oxygen", "Hydrogen", "Helium"), 0),
                Question("Planet known as Red Planet?", listOf("Mars", "Jupiter", "Venus", "Earth"), 0),
                Question("Energy unit?", listOf("Joule", "Watt", "Newton", "Volt"), 0)
            )
            "History" -> listOf(
                Question("Who discovered America?", listOf("Columbus", "Einstein", "Newton", "Gandhi"), 0),
                Question("WWII ended in?", listOf("1945", "1939", "1950", "1941"), 0),
                Question("Great Wall is in?", listOf("China", "Japan", "India", "Mongolia"), 0)
            )
            "Sports" -> listOf(
                Question("How many players in a soccer team?", listOf("11", "10", "9", "12"), 0),
                Question("Olympics held every?", listOf("4 years", "2 years", "3 years", "5 years"), 0),
                Question("Tennis Grand Slam?", listOf("Wimbledon", "Champions League", "NFL", "F1"), 0)
            )
            else -> emptyList()
        }

        _questions.value = questionList
        _currentIndex.value = 0
        _score.value = 0
    }

    fun submitAnswer(answerIndex: Int) {
        val question = _questions.value?.getOrNull(_currentIndex.value ?: 0)
        if (question != null && answerIndex == question.correctAnswerIndex) {
            _score.value = (_score.value ?: 0) + 1
        }
        _currentIndex.value = (_currentIndex.value ?: 0) + 1
    }

    fun isFinished(): Boolean {
        return (_currentIndex.value ?: 0) >= (_questions.value?.size ?: 0)
    }
}
