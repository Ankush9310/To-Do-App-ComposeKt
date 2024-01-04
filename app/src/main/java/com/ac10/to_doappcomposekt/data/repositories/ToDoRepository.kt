package com.ac10.to_doappcomposekt.data.repositories

import com.ac10.to_doappcomposekt.data.roomdb.ToDoDao
import com.ac10.to_doappcomposekt.data.roomdb.ToDoListEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(
    private val toDoDao: ToDoDao
) {

    val getAllTasks: Flow<List<ToDoListEntity>> = toDoDao.getAllTasks()

    val sortByLowPriority: Flow<List<ToDoListEntity>> = toDoDao.sortByLowPriority()

    val sortByHighPriority: Flow<List<ToDoListEntity>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoListEntity> {
        return toDoDao.getSelectedTask(taskId = taskId)
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoListEntity>> {
        return toDoDao.searchDatabase(searchQuery = searchQuery)
    }


    suspend fun addTask(toDoListEntity: ToDoListEntity) {
        return toDoDao.addTask(toDoListEntity = toDoListEntity)
    }

    suspend fun updateTask(toDoListEntity: ToDoListEntity) {
        return toDoDao.updateTask(toDoListEntity = toDoListEntity)
    }

    suspend fun deleteTask(toDoListEntity: ToDoListEntity) {
        return toDoDao.deleteTask(toDoListEntity = toDoListEntity)
    }

    suspend fun deleteAllTask() {
        return toDoDao.deleteAllTasks()
    }


}