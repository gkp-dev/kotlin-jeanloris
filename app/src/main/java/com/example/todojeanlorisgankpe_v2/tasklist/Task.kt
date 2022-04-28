package com.example.todojeanlorisgankpe_v2.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String
) : java.io.Serializable