package com.example.toodle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.toodle.R
import com.example.toodle.data.Task
import com.example.toodle.databinding.DialogTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskBottomSheet(
    private val task: Task? = null,
    private val onSave: (Task) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task?.let {
            binding.etDialogTitle.text = "Edit Task"
            binding.etTitle.setText(it.title)
            binding.etDescription.setText(it.description)
            binding.cbCompleted.isChecked = it.isCompleted
            binding.btnSave.text = "Update"

            when (it.priority) {
                1L -> binding.rbLow.isChecked = true
                2L -> binding.rbMedium.isChecked = true
                3L -> binding.rbHigh.isChecked = true
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty()) {
                binding.etTitle.error = "Please provide a title"
                return@setOnClickListener
            }

            val priority = when (binding.rgPriority.checkedRadioButtonId) {
                R.id.rbLow -> 1
                R.id.rbMedium -> 2
                R.id.rbHigh -> 3
                else -> 1
            }

            val isCompleted = binding.cbCompleted.isChecked

            val finalTask = task?.copy(
                title = title,
                description = description,
                priority = priority.toLong(),
                isCompleted = isCompleted
            ) ?: Task(
                title = title,
                description = description,
                priority = priority.toLong(),
                isCompleted = isCompleted
            )

            onSave(finalTask)
            dismiss()
        }
    }
}