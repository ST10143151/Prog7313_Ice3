package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val listView = view.findViewById<ListView>(R.id.historyList)

        val user = FirebaseAuth.getInstance().currentUser?.displayName ?: "Guest"
        FirebaseFirestore.getInstance()
            .collection("history")
            .whereEqualTo("username", user)
            .get()
            .addOnSuccessListener { result ->
                val history = result.map {
                    val category = it.getString("category")
                    val score = it.getLong("score")
                    val time = it.getLong("timestamp")
                    "$category - Score: $score - Time: $time"
                }
                listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, history)
            }

        return view
    }
}
