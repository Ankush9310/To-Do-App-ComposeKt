package com.ac10.to_doappcomposekt.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ac10.to_doappcomposekt.data.repositories.DataStoreRepository
import com.ac10.to_doappcomposekt.data.repositories.ToDoRepository
import com.ac10.to_doappcomposekt.data.roomdb.ToDoListEntity
import com.ac10.to_doappcomposekt.util.Action
import com.ac10.to_doappcomposekt.util.Constants.MAX_TITLE_LENGTH
import com.ac10.to_doappcomposekt.util.Priority
import com.ac10.to_doappcomposekt.util.RequestState
import com.ac10.to_doappcomposekt.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository, private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    var id by mutableStateOf(0)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.LOW)
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set
    var searchTextState by mutableStateOf("")
        private set

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoListEntity>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoListEntity>>> = _allTasks

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoListEntity>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoListEntity>>> = _searchedTasks

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState


    init {
        getAllTasks()
        readSortState()
    }

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.searchDatabase(
                    searchQuery = "%$searchQuery%"
                ).collect { searchedTasks ->
                    _searchedTasks.value = RequestState.Success(searchedTasks)
                }
            }
        } catch (exception: Exception) {
            _searchedTasks.value = RequestState.Error(exception)
        }

        searchAppBarState = SearchAppBarState.TRIGGERED

    }

    val lowPriorityTasks: StateFlow<List<ToDoListEntity>> =
        toDoRepository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoListEntity>> =
        toDoRepository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map { Priority.valueOf(it) }.collect {
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (exception: Exception) {
            _sortState.value = RequestState.Error(exception)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(
                priority = priority
            )
        }
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                toDoRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (exception: Exception) {
            _allTasks.value = RequestState.Error(exception)
        }
    }


    private val _selectedTask: MutableStateFlow<ToDoListEntity?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoListEntity?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            toDoRepository.getSelectedTask(
                taskId = taskId
            ).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoListEntity = ToDoListEntity(
                title = title,
                description = description,
                priority = priority
            )

            toDoRepository.addTask(
                toDoListEntity = toDoListEntity
            )

        }

        searchAppBarState = SearchAppBarState.CLOSED

    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoListEntity = ToDoListEntity(
                id = id,
                title = title,
                description = description,
                priority = priority
            )

            toDoRepository.updateTask(
                toDoListEntity = toDoListEntity
            )
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoListEntity = ToDoListEntity(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            toDoRepository.deleteTask(
                toDoListEntity = toDoListEntity
            )
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAllTask()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAllTasks()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
    }


    fun updateTaskFields(selectedTask: ToDoListEntity?) {
        if (selectedTask != null) {
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }


    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateAppBarState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    fun updateSearchText(newText: String) {
        searchTextState = newText
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }


}