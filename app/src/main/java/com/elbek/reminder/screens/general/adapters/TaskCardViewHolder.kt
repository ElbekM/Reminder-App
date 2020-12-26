package com.elbek.reminder.screens.general.adapters

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.common.utils.Utils.validateTaskCount
import com.elbek.reminder.databinding.ViewAddTasklistBinding
import com.elbek.reminder.databinding.ViewTaskCardBinding

class TaskCardViewHolder(
    private val binding: ViewTaskCardBinding,
    private val itemClicked: (Pair<TaskCardType, Int>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskCardItem) = with(binding) {
        //taskListIconImageView = item.icon
        taskListNameTextView.text = item.title
        taskCountTextView.text = validateTaskCount(item.taskCount ?: 0)
        taskListProgressBar.progress = item.progress ?: 0
        taskListProgressTextView.text = item.progress.toString()

        itemView.setOnClickListener { itemClicked(item.cardType to adapterPosition) }
    }
}

class TaskCardAddViewHolder(
    private val binding: ViewAddTasklistBinding,
    private val itemClicked: (Pair<TaskCardType, Int>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskCardItem) {
        itemView.setOnClickListener { itemClicked(item.cardType to adapterPosition) }
    }
}
