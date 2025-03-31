package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class CategoryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val categories = listOf("General Knowledge", "Science", "History", "Sports")
        val buttons = listOf(
            view.findViewById<Button>(R.id.category1),
            view.findViewById<Button>(R.id.category2),
            view.findViewById<Button>(R.id.category3),
            view.findViewById<Button>(R.id.category4)
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("category", categories[index])
                findNavController().navigate(R.id.action_category_to_quiz, bundle)
            }
        }

        view.findViewById<Button>(R.id.historyButton).setOnClickListener {
            findNavController().navigate(R.id.action_category_to_history)
        }

        view.findViewById<Button>(R.id.leaderboardButton).setOnClickListener {
            findNavController().navigate(R.id.action_category_to_leaderboard)
        }

        return view
    }
}
