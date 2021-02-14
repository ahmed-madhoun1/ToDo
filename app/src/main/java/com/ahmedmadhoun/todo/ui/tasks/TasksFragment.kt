package com.ahmedmadhoun.todo.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmadhoun.todo.R
import com.ahmedmadhoun.todo.data.SortOrder
import com.ahmedmadhoun.todo.databinding.FragmentTasksBinding
import com.ahmedmadhoun.todo.util.onQueryTextChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)

        val tasksAdapter = TasksAdapter()

        binding.apply {
            recyclerViewTasks.apply {
                adapter = tasksAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            tasksAdapter.submitList(it)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)

        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChange {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.sort_by_name_item -> {
                viewModel.onSortOrderSelected(SortOrder.BY_TITLE)
                true
            }
            R.id.sort_by_date_created_item -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE_CREATED)
                true
            }
            R.id.hide_completed_item -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedSelected(item.isChecked)
                true
            }
            R.id.delete_all_completed_item -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}