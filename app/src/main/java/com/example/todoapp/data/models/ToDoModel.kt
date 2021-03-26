package com.example.todoapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class ToDoModel(
    val title: String,
    val priority: Priority,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable
