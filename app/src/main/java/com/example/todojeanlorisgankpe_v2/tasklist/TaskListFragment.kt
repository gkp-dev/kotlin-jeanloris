package com.example.todojeanlorisgankpe_v2.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todojeanlorisgankpe_v2.R
import com.example.todojeanlorisgankpe_v2.databinding.FragmentTaskListBinding
import com.example.todojeanlorisgankpe_v2.form.FormActivity
import com.example.todojeanlorisgankpe_v2.network.Api
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val adapter = TaskListAdapter()
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var binding: FragmentTaskListBinding

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ici on récupérera le résultat pour le traiter
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        viewModel.create(task)
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ici on récupérera le résultat pour le traiter
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        viewModel.update(task)
    }


    override fun onResume() {
        super.onResume()

        viewModel.refresh()

        lifecycleScope.launch {
            // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
            val userInfo = Api.userWebService.getInfo().body()!!

            var userInfoText = view?.findViewById<TextView>(R.id.userInfoText)
            userInfoText?.text = "${userInfo.firstName} ${userInfo.lastName}"
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }

        binding.recyclerView.adapter = adapter

        adapter.onClickDelete =  { task ->
            viewModel.delete(task)

        }

        adapter.onClickEdit = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }

        binding.floatingActionButton.setOnClickListener() {
            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
            //val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
        }




    }





}