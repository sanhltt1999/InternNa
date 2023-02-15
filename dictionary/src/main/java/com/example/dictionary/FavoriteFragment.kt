package com.example.dictionary

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var adapter: WordAdapter

private lateinit var recyclerView: RecyclerView
private lateinit var words: ArrayList<Word>
private lateinit var databaseAccess: DatabaseAccess

class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rvFaWord)


        context?.let {
            databaseAccess = DatabaseAccess(it)
            words = databaseAccess.getInstance(it).getFavoriteWord()
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = WordAdapter(words)
        recyclerView.adapter = adapter
        adapter.onWordClick = {
            it.history = 1
            databaseAccess.update(it)
            val intent = Intent(this@FavoriteFragment.requireContext(), DetailActivity::class.java)
            intent.putExtra("word", it)
            startActivity(intent)
        }

    }
}