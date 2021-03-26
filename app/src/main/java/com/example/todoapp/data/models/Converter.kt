package com.example.todoapp.data.models

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String =
        priority.name

    @TypeConverter
    fun toPriority(priority: String): Priority =
        Priority.valueOf(priority)
}