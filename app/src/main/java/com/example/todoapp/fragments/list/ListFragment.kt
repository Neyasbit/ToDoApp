package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.example.todoapp.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }

    private var binding: FragmentListBinding? = null
    private lateinit var todoListAdapter: TodoListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentListBinding.inflate(layoutInflater, container, false)
        binding = fragmentBinding

        setHasOptionsMenu(true)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoListAdapter = TodoListAdapter()

        binding?.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = todoListAdapter
            }
            swipeToDeleteModel(recyclerView)
            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }

        toDoViewModel.allData.observe(requireActivity()) { models ->
            models.let { todoListAdapter.submitList(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAllModels()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllModels() {
        toDoViewModel.deleteAllModel()
    }

    private fun swipeToDeleteModel(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDeleteItem() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // deleted item
                val deletedModel = todoListAdapter.currentList[viewHolder.adapterPosition]
                toDoViewModel.deleteModel(deletedModel)
                todoListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                // restore item
                restoreDeletedItem(viewHolder.itemView, deletedModel, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoModel, position: Int) {
        Snackbar.make(
            view,
            "Deleted ${deletedItem.title}",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction("Undo") {
                toDoViewModel.insertData(deletedItem)
                todoListAdapter.notifyItemChanged(position)
            }
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}