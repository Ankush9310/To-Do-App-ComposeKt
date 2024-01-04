package com.ac10.to_doappcomposekt.data.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ac10.to_doappcomposekt.util.Constants.DATABASE_TABLE
import com.ac10.to_doappcomposekt.util.Priority

@Entity(tableName = DATABASE_TABLE)
data class ToDoListEntity(
    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority,
)