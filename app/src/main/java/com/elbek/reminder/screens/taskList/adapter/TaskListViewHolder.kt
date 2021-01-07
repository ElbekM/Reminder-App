package com.elbek.reminder.screens.taskList.adapter

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.R
import com.elbek.reminder.common.extensions.setStrikeFlag
import com.elbek.reminder.common.extensions.setTint
import com.elbek.reminder.databinding.ViewTaskItemBinding

class TaskListViewHolder(
    private val binding: ViewTaskItemBinding,
    private val itemClicked: (Pair<String, TaskClickType>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskListItem) = with(binding) {
        taskTitleTextView.apply {
            setStrikeFlag(item.isCompleted)
            text = item.name
        }
        taskSubtitleTextView.text = item.taskCompleted

        importantImageView.apply {
            if (item.isImportant) setTint(R.color.colorWhite)
            else setTint(R.color.colorPrimaryDark)
            setOnClickListener { itemClicked(item.taskId to TaskClickType.IMPORTANT) }
        }

        taskCheckbox.apply {
            isChecked = item.isCompleted
            setOnClickListener {
                isChecked = !item.isCompleted
                itemClicked(item.taskId to TaskClickType.CHECKBOX)
            }
        }

        itemView.setOnClickListener { itemClicked(item.taskId to TaskClickType.TASK) }
    }
}
