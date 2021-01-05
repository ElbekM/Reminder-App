package com.elbek.reminder.screens.general.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.databinding.ViewTaskTypeItemBinding
import com.elbek.reminder.screens.general.TaskType

//TODO: Adapter via Kotlin delegate
class TaskTypeAdapter(private val itemClicked: (TaskType) -> Unit) : RecyclerView.Adapter<TaskTypeViewHolder>() {

    private var items: List<TaskTypeItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTypeViewHolder =
        TaskTypeViewHolder(
            ViewTaskTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClicked
        )

    override fun onBindViewHolder(holder: TaskTypeViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun setItems(taskTypeItems: List<TaskTypeItem>) {
        items = taskTypeItems
        notifyDataSetChanged()
    }
}
