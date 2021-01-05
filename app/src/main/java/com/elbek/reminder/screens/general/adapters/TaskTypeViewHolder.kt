package com.elbek.reminder.screens.general.adapters

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.common.utils.Utils.validateTaskCount
import com.elbek.reminder.databinding.ViewTaskTypeItemBinding
import com.elbek.reminder.screens.general.TaskType

class TaskTypeViewHolder(
    private val binding: ViewTaskTypeItemBinding,
    private val itemClicked: (TaskType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskTypeItem) = with(binding) {
        //taskTypeImageView = item.icon
        taskTypeTitleTextView.text = item.title
        taskTypeSubtitleTextView.text = validateTaskCount(item.taskCount)

        itemView.setOnClickListener { itemClicked(item.type) }
    }
}
