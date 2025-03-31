package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizFragment : Fragment() {
    private lateinit var viewModel: QuizViewModel
    private lateinit var questionText: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var progressBar: ProgressBar
    private var startTime = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        val category = arguments?.getString("category") ?: "General Knowledge"

        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        viewModel.loadQuestions(category)

        questionText = view.findViewById(R.id.questionText)
        optionButtons = listOf(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )
        progressBar = view.findViewById(R.id.seekBar)

        startTime = System.currentTimeMillis()
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            questionText.text = question.text
            optionButtons.forEachIndexed { index, btn -> btn.text = question.options[index] }
            progressBar.progress = viewModel.progress()
        }

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.answer(index)
                if (viewModel.hasNext()) viewModel.nextQuestion()
                else {
                    val score = viewModel.calculateScore(System.currentTimeMillis() - startTime)
                    saveScore(category, score, viewModel.correctCount)
                    findNavController().navigate(R.id.action_quiz_to_category)
                }
            }
        }

        return view
    }

    private fun saveScore(category: String, score: Int, correctAnswers: Int) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest"
        db.collection("history").add(
            mapOf(
                "username" to user,
                "category" to category,
                "score" to score,
                "correctAnswers" to correctAnswers,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}
