package com.example.todojeanlorisgankpe_v2.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.todojeanlorisgankpe_v2.R
import com.example.todojeanlorisgankpe_v2.databinding.FragmentTaskListBinding
import com.example.todojeanlorisgankpe_v2.form.FormActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.util.*

class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
    )

    private val adapter = TaskListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        adapter.submitList(taskList)

        adapter.onClickDelete =  { task ->
            taskList = taskList - task
            binding.recyclerView.adapter = adapter
            adapter.submitList(taskList)
        }

        val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // ici on récupérera le résultat pour le traiter
            val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
            taskList = taskList + task
            binding.recyclerView.adapter = adapter
            adapter.submitList(taskList)
        }

        binding.floatingActionButton.setOnClickListener() {
            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
            //val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")


        }


    }


}