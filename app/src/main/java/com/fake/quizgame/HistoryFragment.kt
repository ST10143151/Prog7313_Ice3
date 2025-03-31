package com.fake.quizgame

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(historyList)
        recyclerView.adapter = adapter

        loadHistory()

        return view
    }

    private fun loadHistory() {
        FirebaseFirestore.getInstance().collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                historyList.clear()
                for (doc in result) {
                    val entry = HistoryEntry(
                        username = doc.getString("username") ?: "Unknown",
                        category = doc.getString("category") ?: "Unknown",
                        score = doc.getLong("score")?.toInt() ?: 0,
                        correctAnswers = doc.getLong("correctAnswers")?.toInt() ?: 0,
                        timestamp = doc.getLong("timestamp") ?: 0
                    )
                    historyList.add(entry)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT).show()
            }
    }
}
