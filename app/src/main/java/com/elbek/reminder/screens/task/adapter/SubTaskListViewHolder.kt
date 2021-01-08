package com.elbek.reminder.screens.task.adapter

import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.common.extensions.setStrikeFlag
import com.elbek.reminder.databinding.ViewSubtaskItemBinding

class SubTaskListViewHolder(
    private val binding: ViewSubtaskItemBinding,
    private val itemClicked: (Pair<Int, SubTaskClickType>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SubTaskListItem) = with(binding) {
        subTaskTitleTextView.apply {
            setStrikeFlag(item.isCompleted)
            text = item.name
        }

        subTaskCheckbox.apply {
            isChecked = item.isCompleted
            setOnClickListener {
                isChecked = !item.isCompleted
                itemClicked(adapterPosition to SubTaskClickType.CHECKBOX)
            }
        }

        deleteSubTaskImageView.setOnClickListener { itemClicked(adapterPosition to SubTaskClickType.REMOVE) }
    }
}
