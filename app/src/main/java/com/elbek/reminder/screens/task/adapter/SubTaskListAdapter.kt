package com.elbek.reminder.screens.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.databinding.ViewSubtaskItemBinding

class SubTaskListAdapter(
    private val itemClicked: ((Pair<Int, SubTaskClickType>)) -> Unit
) : RecyclerView.Adapter<SubTaskListViewHolder>() {

    private var items: List<SubTaskListItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskListViewHolder =
        SubTaskListViewHolder(
            ViewSubtaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClicked
        )

    override fun onBindViewHolder(holder: SubTaskListViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun setItems(subTaskList: List<SubTaskListItem>) {
        items = subTaskList
        notifyDataSetChanged()
    }
}
