package com.ac10.to_doappcomposekt.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoListEntity::class],
    version = 1,
)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

}