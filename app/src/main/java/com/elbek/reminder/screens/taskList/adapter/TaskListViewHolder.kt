package com.elbek.reminder.screens.taskList.adapter

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.databinding.ViewTaskItemBinding

class TaskListViewHolder(
    private val binding: ViewTaskItemBinding,
    private val itemClicked: (Pair<Int, TaskClickType>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskListItem) = with(binding) {
        //TODO: implement important logic
        taskTitleTextView.text = item.name
        taskSubtitleTextView.text = item.taskCompleted
        taskCheckbox.isChecked = item.isCompleted
        //importantImageView = item.isImportant

        itemView.setOnClickListener { itemClicked(adapterPosition to TaskClickType.TASK) }
        importantImageView.setOnClickListener { itemClicked(adapterPosition to TaskClickType.IMPORTANT) }
        taskCheckbox.apply {
            setOnClickListener {
                isChecked = !item.isCompleted
                itemClicked(adapterPosition to TaskClickType.CHECKBOX)
            }
        }
    }
}
