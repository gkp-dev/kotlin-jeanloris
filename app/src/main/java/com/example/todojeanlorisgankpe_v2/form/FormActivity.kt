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

        var button = findViewById<Button>(R.id.button)
        var title = findViewById<EditText>(R.id.plain_text_input).text
        var description = findViewById<EditText>(R.id.plain_text_input2).text
        button.setOnClickListener(){
            val newTask = Task(UUID.randomUUID().toString(), title.toString(), description.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}