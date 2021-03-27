package com.example.todoapp.fragments.list

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.databinding.RowLayoutBinding


class TodoListAdapter :
    ListAdapter<ToDoModel, TodoListAdapter.ToDoViewHolder>(ToDoListComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ToDoViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoModel: ToDoModel) {
            val colorPosition = toDoModel.priority.ordinal
            binding.apply {
                titleTxt.text = toDoModel.title
                descriptionTxt.text = toDoModel.description
                rowBackground.setOnClickListener {
                    val action =
                        ListFragmentDirections.actionListFragmentToUpdateFragment(toDoModel)
                    it.findNavController().navigate(action)
                }
                binding.priorityIndicator.backgroundTintList =
                    ColorStateList.valueOf(Priority.findColorByPosition(colorPosition))
            }
        }

        companion object {
            fun create(parent: ViewGroup): ToDoViewHolder {
                val view = RowLayoutBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return ToDoViewHolder(view)
            }
        }
    }

    class ToDoListComparator : DiffUtil.ItemCallback<ToDoModel>() {
        override fun areItemsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.priority == newItem.priority
                    && oldItem.id == newItem.id
        }
    }
}

abstract class SwipeToDeleteItem : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}


