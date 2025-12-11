package com.example.toodle.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toodle.R
import com.example.toodle.data.Task
import com.example.toodle.data.TaskDatabase
import com.example.toodle.databinding.ActivityMainBinding
import com.example.toodle.databinding.DialogTaskBinding
import com.example.toodle.repository.TaskRepository
import com.example.toodle.ui.adapter.TaskAdapter
import com.example.toodle.viewmodel.TaskViewModel
import com.example.toodle.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerViews()
        setupClickListeners()
        setupHeader()
        observeTasks()
    }

    private fun setupViewModel() {
        val database = TaskDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())

        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        adapter = TaskAdapter(
            onItemClick = { task ->
                // CHANGED: Open BottomSheet instead of Dialog
                openTaskBottomSheet(task)
            },
            onLongItemClick = { task ->
                showDeleteConfirmation(task)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun openTaskBottomSheet(existingTask: Task?) {
        val bottomSheet = TaskBottomSheet(existingTask) { finalTask ->
            if (existingTask != null) {
                viewModel.updateTask(finalTask)
            } else {
                viewModel.insertTask(finalTask)
            }
        }
        bottomSheet.show(supportFragmentManager, "TaskBottomSheet")
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("You're about to delete '${task.title}'")
            .setPositiveButton("Delete"){_,_->
                viewModel.deleteTask(task)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeTasks() {
        viewModel.allTasks.observe(this){tasks ->
            adapter.updateTasks(tasks)
            if(tasks.isEmpty()){
                binding.rvTasks.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.rvTasks.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners(){
        binding.btnAddTask.setOnClickListener {
            // CHANGED: Open BottomSheet for new task
            openTaskBottomSheet(null)
        }
    }

    private fun setupHeader() {
        val sharedPref = getSharedPreferences("ToodlePrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", null)

        if (userName != null) {
            binding.tvHeaderTitle.text = "Hi $userName!"
        } else {
            showNameInputDialog(sharedPref) // ask for user's name if not previously provided
        }
    }

    private fun showNameInputDialog(sharedPref: android.content.SharedPreferences) {
        val input = android.widget.EditText(this)
        input.hint = "Enter your name"

        val container = android.widget.FrameLayout(this)
        val params = android.widget.FrameLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 20, 50, 20)
        input.layoutParams = params
        container.addView(input)

        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("Please enter your name")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val name = input.text.toString()
                if (name.isNotEmpty()) {
                    with(sharedPref.edit()) {
                        putString("USER_NAME", name)
                        apply()
                    }

                    binding.tvHeaderTitle.text = "Hi $name!"
                } else {
                    // fallback for no name given
                    binding.tvHeaderTitle.text = "Hi Friend!"
                }
            }
            .show()
    }


    private fun showTaskDialog(existingTask: Task? = null) {
        val dialogTaskBinding = DialogTaskBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogTaskBinding.root)
            .create()

        existingTask?.let { task ->
            dialogTaskBinding.etTitle.setText(task.title)
            dialogTaskBinding.etDescription.setText(task.description)
            dialogTaskBinding.cbCompleted.isChecked = task.isCompleted

            when(task.priority){
                1L -> dialogTaskBinding.rbLow.isChecked = true
                2L -> dialogTaskBinding.rbMedium.isChecked = true
                3L -> dialogTaskBinding.rbLow.isChecked = true
            }

            dialogTaskBinding.btnSave.text ="Update"
        }

        dialogTaskBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogTaskBinding.btnSave.setOnClickListener {
            val title = dialogTaskBinding.etTitle.text.toString().trim()
            val description = dialogTaskBinding.etDescription.text.toString().trim()
            if(title.isEmpty()){
                dialogTaskBinding.etTitle.error = "Please provide a title"
                return@setOnClickListener
            }

            val priority = when(dialogTaskBinding.rgPriority.checkedRadioButtonId){
                R.id.rbLow -> 1
                R.id.rbMedium -> 2
                R.id.rbHigh -> 3
                else -> 1
            }

            val isCompleted = dialogTaskBinding.cbCompleted.isChecked

            val task = existingTask?.copy(
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

            if(existingTask !=null){
                viewModel.updateTask(task)
            } else {
                viewModel.insertTask(task)
            }

            dialog.dismiss()
        }
        dialog.show()

    }

}


