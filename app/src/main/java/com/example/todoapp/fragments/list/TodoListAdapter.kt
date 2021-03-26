package com.example.todoapp.fragments.list

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoModel


class TodoListAdapter :
    ListAdapter<ToDoModel, TodoListAdapter.ToDoViewHolder>(ToDoListComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleItem: TextView = itemView.findViewById(R.id.title_txt)
        private val descriptionItem: TextView = itemView.findViewById(R.id.description_txt)
        private val priorityCircle: View = itemView.findViewById(R.id.priority_indicator)
        private val rowLayout: ConstraintLayout = itemView.findViewById(R.id.row_background)


        fun bind(toDoModel: ToDoModel) {
            titleItem.text = toDoModel.title
            descriptionItem.text = toDoModel.description

            when (toDoModel.priority) {
                Priority.High -> priorityCircle.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.red))
                Priority.Medium -> priorityCircle.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.yellow))
                else -> priorityCircle.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green
                        )
                    )
            }

            rowLayout.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoModel)
                itemView.findNavController().navigate(action)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ToDoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_layout, parent, false)
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
        }
    }
}


