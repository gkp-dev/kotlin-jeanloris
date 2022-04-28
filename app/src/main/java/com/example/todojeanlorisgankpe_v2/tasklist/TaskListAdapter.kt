package com.example.todojeanlorisgankpe_v2.tasklist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todojeanlorisgankpe_v2.R
import com.example.todojeanlorisgankpe_v2.form.FormActivity

class TaskListAdapter : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(ItemsDiffCallback) {

    object ItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return oldItem == newItem
        }
    }

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            // on affichera les données ici
            val textView = itemView.findViewById<TextView>(R.id.task_title)
            textView.text = task.title
            val textDescription = itemView.findViewById<TextView>(R.id.task_description)
            textDescription.text = task.description
            val imageButton = itemView.findViewById<ImageButton>(R.id.deleteTask)
            imageButton.setOnClickListener {
                onClickDelete(task)
            }
            val editButton = itemView.findViewById<ImageButton>(R.id.editTask)
            editButton.setOnClickListener(){
                onClickEdit(task)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

}