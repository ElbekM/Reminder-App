package com.elbek.reminder.screens.general.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elbek.reminder.R
import com.elbek.reminder.databinding.ViewAddTasklistBinding
import com.elbek.reminder.databinding.ViewTaskCardBinding
import java.lang.IllegalArgumentException

//TODO: Adapter via Kotlin delegate
class TaskCardAdapter(private val itemClicked: (Pair<TaskCardType, Int>) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<TaskCardItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LayoutInflater.from(parent.context).let { layoutInflater ->
            when (viewType) {
                R.layout.view_task_card ->
                    TaskCardViewHolder(
                        ViewTaskCardBinding.inflate(layoutInflater, parent, false),
                        itemClicked
                    )
                R.layout.view_add_tasklist ->
                    TaskCardAddViewHolder(
                        ViewAddTasklistBinding.inflate(layoutInflater, parent, false),
                        itemClicked
                    )
                else -> throw IllegalArgumentException()
            }
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        items!![position].let {
            when (holder) {
                is TaskCardViewHolder -> holder.bind(it)
                is TaskCardAddViewHolder -> holder.bind(it)
            }
        }

    override fun getItemViewType(position: Int): Int = when (items!![position].cardType) {
        TaskCardType.TASK_LIST -> R.layout.view_task_card
        TaskCardType.ADD -> R.layout.view_add_tasklist
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun setItems(taskCardItems: List<TaskCardItem>) {
        items = taskCardItems
        notifyDataSetChanged()
    }
}
