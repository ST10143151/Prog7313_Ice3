package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class LeaderboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        val listView = view.findViewById<ListView>(R.id.leaderboardList)

        FirebaseFirestore.getInstance().collection("history")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                val data = result.map {
                    val name = it.getString("username")
                    val score = it.getLong("score")
                    "$name - $score"
                }
                listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
            }

        return view
    }
}
