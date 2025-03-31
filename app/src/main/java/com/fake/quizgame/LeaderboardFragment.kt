package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LeaderboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        val leaderboardList = view.findViewById<ListView>(R.id.leaderboardList)

        FirebaseFirestore.getInstance()
            .collection("history")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                val leaders = result.map {
                    val user = it.getString("username") ?: "Guest"
                    val category = it.getString("category") ?: "Unknown"
                    val score = it.getLong("score") ?: 0
                    "$user - $category - Score: $score"
                }

                leaderboardList.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, leaders)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load leaderboard", Toast.LENGTH_SHORT).show()
            }

        return view
    }
}
