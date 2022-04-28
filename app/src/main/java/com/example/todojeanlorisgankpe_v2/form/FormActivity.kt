package com.example.todojeanlorisgankpe_v2.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.todojeanlorisgankpe_v2.R
import com.example.todojeanlorisgankpe_v2.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        var task = intent.getSerializableExtra("task") as Task?
        var button = findViewById<Button>(R.id.addTaskButton)
        var title = findViewById<EditText>(R.id.plain_text_input)
        var description = findViewById<EditText>(R.id.plain_text_input2)

        title.setText(task?.title)
        description.setText(task?.description)

        button.setOnClickListener {
            val newTask = Task(id = task?.id ?:UUID.randomUUID().toString(), title.text.toString(), description.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}