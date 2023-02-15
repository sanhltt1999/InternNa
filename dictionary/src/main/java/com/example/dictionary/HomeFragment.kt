package com.example.dictionary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: WordAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var words: ArrayList<Word>
    private lateinit var databaseAccess: DatabaseAccess
    private lateinit var menuItem: MenuItem
    private lateinit var searchView: SearchView

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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rvWord)

        context?.let {
            databaseAccess = DatabaseAccess(it)
            words = databaseAccess.getInstance(it).getAllWords()
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = WordAdapter(words)
        recyclerView.adapter = adapter
        adapter.onWordClick = {
            it.history = 1
            databaseAccess.update(it)
            val intent = Intent(this@HomeFragment.requireContext(), DetailActivity::class.java)
            intent.putExtra("word", it)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        menuItem = menu.findItem(R.id.search)
        searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                filterList(query)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Word>()
            for (i in words) {
                if (i.en_Word?.lowercase()?.startsWith(query, false) == true) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Log.d(query, "noo")
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

}