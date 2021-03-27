package com.example.todoapp.data.models

import android.graphics.Color

enum class Priority(val priorityName: String, val color: String) {
    High("High", "#FF4646"),
    Medium("Medium", "#FFC114"),
    Low("Low", "#00C980");

    companion object {
        fun findColorByPosition(position: Int): Int {
            for (enum in values()) {
                if (position == enum.ordinal)
                    return Color.parseColor(enum.color)
            }
            return 0
        }
    }
}
