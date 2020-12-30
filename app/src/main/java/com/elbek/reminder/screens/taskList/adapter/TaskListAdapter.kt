package com.elbek.reminder.screens.taskList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.databinding.ViewTaskItemBinding

class TaskListAdapter(
    private val itemClicked: ((Pair<Int, TaskClickType>)) -> Unit
) : RecyclerView.Adapter<TaskListViewHolder>() {

    private var items: List<TaskListItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder =
        TaskListViewHolder(
            ViewTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClicked
        )

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun setItems(taskList: List<TaskListItem>) {
        items = taskList
        notifyDataSetChanged()
    }
}
