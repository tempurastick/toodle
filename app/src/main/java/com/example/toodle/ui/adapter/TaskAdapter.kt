package com.example.toodle.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.toodle.R
import com.example.toodle.data.Task
import com.example.toodle.databinding.ItemTaskBinding

class TaskAdapter(
    private var task: List<Task> = emptyList(),
    private val onItemClick: (Task) -> Unit,
    private val onLongItemClick: (Task) -> Unit,
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        // take one task from all the tasks
        val task = task[position]
        holder.bind(task, onItemClick, onLongItemClick)
    }

    fun updateTasks(newTask: List<Task>) {
        task = newTask
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = task.size

    class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(task: Task, onItemClick: (Task) -> Unit, onLongItemClick: (Task) -> Unit) {
            val context = binding.root.context

            binding.tvTaskTitle.text = task.title
            binding.tvTaskDescription.text = task.description

            // priority label
            binding.chipPriority.text = task.getPriorityText()
            binding.chipPriority.chipBackgroundColor = ColorStateList.valueOf(task.getPriorityColor().toInt())
            binding.viewPriorityIndicator.setBackgroundColor(task.getPriorityColor().toInt())
            binding.chipPriority.chipIcon = ContextCompat.getDrawable(context, R.drawable.outline_label_important_12)


            // status label
            if(task.isCompleted) {
                binding.chipStatus.text = "Completed"
                binding.chipStatus.chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.green)
                binding.chipStatus.chipIcon = ContextCompat.getDrawable(context, R.drawable.outline_check_small_12)
                binding.chipStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.chipStatus.chipIconTint = ContextCompat.getColorStateList(context, R.color.white)
                binding.tvTaskTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.chipStatus.text = "Pending"
                binding.chipStatus.chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.primary_200)
                binding.chipStatus.chipIcon = ContextCompat.getDrawable(context, R.drawable.twotone_horizontal_rule_24)
                binding.chipStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.chipStatus.chipIconTint = ContextCompat.getColorStateList(context, R.color.white)
                binding.tvTaskTitle.setTextColor(ContextCompat.getColor(context, R.color.black)) // resetting title
            }

            binding.root.setOnLongClickListener {
                onLongItemClick(task)
                true
            }
            binding.root.setOnClickListener {
                onItemClick(task)
            }

        }
    }
}