package com.ahmedmadhoun.todo.ui.addedittasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ahmedmadhoun.todo.R
import com.ahmedmadhoun.todo.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            editTextTaskTitle.setText(viewModel.taskTitle)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState() // No Animation
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = viewModel.task?.createdDateFormatted
        }

    }

}