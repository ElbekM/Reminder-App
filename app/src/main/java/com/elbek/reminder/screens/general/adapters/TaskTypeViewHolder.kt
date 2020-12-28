package com.elbek.reminder.screens.general.adapters

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.common.utils.Utils.validateTaskCount
import com.elbek.reminder.databinding.ViewTaskTypeItemBinding

class TaskTypeViewHolder(
    private val binding: ViewTaskTypeItemBinding,
    private val itemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskTypeItem) = with(binding) {
        //taskTypeImageView = item.icon
        taskTypeTitleTextView.text = item.title
        taskTypeSubtitleTextView.text = validateTaskCount(item.taskCount)

        itemView.setOnClickListener { itemClicked(adapterPosition) }
    }
}
