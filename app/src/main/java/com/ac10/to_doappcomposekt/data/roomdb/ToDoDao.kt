package com.ac10.to_doappcomposekt.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoListEntity: ToDoListEntity)

    @Update
    suspend fun updateTask(toDoListEntity: ToDoListEntity)

    @Delete
    suspend fun deleteTask(toDoListEntity: ToDoListEntity)


    @Query("SELECT * FROM to_do_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<ToDoListEntity>>

    @Query("SELECT * FROM to_do_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoListEntity>

    @Query("DELETE FROM to_do_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM to_do_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoListEntity>>

    @Query(
        """
        SELECT * FROM to_do_table ORDER BY
    CASE
        WHEN priority LIKE 'L%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'H%' THEN 3
    END
    """
    )
    fun sortByLowPriority(): Flow<List<ToDoListEntity>>

    @Query(
        """
        SELECT * FROM to_do_table ORDER BY
    CASE
        WHEN priority LIKE 'H%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'L%' THEN 3
    END
    """
    )
    fun sortByHighPriority(): Flow<List<ToDoListEntity>>


}