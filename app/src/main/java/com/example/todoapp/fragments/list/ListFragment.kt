package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {
    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView: RecyclerView
        val todoListAdapter = TodoListAdapter()

        view.apply {
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = todoListAdapter
            }
        }
        toDoViewModel.allData.observe(requireActivity()) { models ->
            models.let { todoListAdapter.submitList(it) }
        }
        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> deleteAllModels()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllModels() {
        toDoViewModel.deleteAllModel()
    }
}