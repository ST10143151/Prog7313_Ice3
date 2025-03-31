package com.fake.quizgame

import android.os.Bundle
import android.util.Log
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
    private lateinit var quizCategory: TextView
    private var startTime = 0L
    private lateinit var category: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        category = arguments?.getString("category") ?: "General Knowledge"
        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        viewModel.loadQuestions(category)

        quizCategory = view.findViewById(R.id.quizCategory)
        questionText = view.findViewById(R.id.questionText)
        optionButtons = listOf(
            view.findViewById(R.id.optionA),
            view.findViewById(R.id.optionB),
            view.findViewById(R.id.optionC),
            view.findViewById(R.id.optionD)
        )
        progressBar = view.findViewById(R.id.progressBar)

        quizCategory.text = category
        startTime = System.currentTimeMillis()

        viewModel.questions.observe(viewLifecycleOwner) {
            updateUI()
        }

        return view
    }

    private fun updateUI() {
        val index = viewModel.currentIndex.value ?: 0
        val question = viewModel.questions.value?.getOrNull(index)

        if (question != null) {
            questionText.text = question.text
            optionButtons.forEachIndexed { i, btn ->
                btn.text = question.options[i]
                btn.setOnClickListener {
                    viewModel.submitAnswer(i)
                    if (viewModel.isFinished()) {
                        onQuizFinished()
                    } else {
                        updateUI()
                    }
                }
            }
            progressBar.max = viewModel.questions.value?.size ?: 0
            progressBar.progress = index + 1
        }
    }

    private fun onQuizFinished() {
        val elapsedTime = System.currentTimeMillis() - startTime
        val score = viewModel.score.value ?: 0
        val correctAnswers = score
        val user = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest"

        Log.d("QuizSave", "Saving score: $score, correct: $correctAnswers")

        FirebaseFirestore.getInstance().collection("history")
            .add(
                mapOf(
                    "username" to user,
                    "category" to category,
                    "score" to score,
                    "correctAnswers" to correctAnswers,
                    "timestamp" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                Log.d("QuizSave", "Saved to Firestore!")
                val bundle = Bundle().apply {
                    putInt("score", score)
                    putInt("total", viewModel.questions.value?.size ?: 0)
                }
                findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment, bundle)
            }
            .addOnFailureListener { e ->
                Log.e("QuizSave", "Error saving: ", e)
                Toast.makeText(requireContext(), "Failed to save score", Toast.LENGTH_SHORT).show()
            }
    }
}
