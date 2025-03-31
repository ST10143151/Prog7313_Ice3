package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class QuizResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_quiz_result, container, false)

        val score = arguments?.getInt("score") ?: 0
        val total = arguments?.getInt("total") ?: 0

        val resultText = view.findViewById<TextView>(R.id.resultText)
        val backButton = view.findViewById<Button>(R.id.backButton)

        // Uses string resource
        resultText.text = getString(R.string.quiz_score_message, score, total)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizResultFragment_to_categoryFragment)
        }

        return view
    }
}
